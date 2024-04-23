package com.bagusmwicaksono.hermione.members.service;

import com.bagusmwicaksono.hermione.members.controller.dto.MembersDto;
import com.bagusmwicaksono.hermione.members.exception.CredentialNotFoundException;
import com.bagusmwicaksono.hermione.members.exception.DuplicatedCredentialException;
import com.bagusmwicaksono.hermione.members.model.Credentials;
import com.bagusmwicaksono.hermione.members.model.Members;
import com.bagusmwicaksono.hermione.members.repository.CredentialsRepository;
import com.bagusmwicaksono.hermione.members.repository.MembersRepository;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final MembersRepository membersRepository;

    public CredentialsService(CredentialsRepository credentialsRepository, MembersRepository membersRepository) {
        this.credentialsRepository = credentialsRepository;
        this.membersRepository = membersRepository;
    }
    public Mono<MembersDto> performCreateCredential(MembersDto newMembersDto){
        log.info("[CredentialsService] performCreateCredential");
        return credentialsRepository.existsByEmail(newMembersDto.getEmail()).flatMap(result->{
            if(Boolean.TRUE.equals(result)){
                return Mono.error(new DuplicatedCredentialException(newMembersDto.getEmail()));
            }
            Credentials credentials = new Credentials();
            BeanUtils.copyProperties(newMembersDto, credentials);

            Members newMember=Members.builder()
                    .fullname(newMembersDto.getUsername())
                    .initial(getInitial(newMembersDto.getUsername()))
                    .status("ACTIVE")
                    .build();
            return membersRepository.save(newMember).flatMap(savedMember ->{
                credentials.setMemberId(savedMember.getId());
                return credentialsRepository.save(credentials).map(saveCred ->{
                    MembersDto credentialsDto = new MembersDto();
                    BeanUtils.copyProperties(saveCred, credentialsDto);
                    credentialsDto.setCredId(saveCred.getId());
                    credentialsDto.setId(savedMember.getId());
                    return credentialsDto;
                });
            });
        });
    }
    private String getInitial(String name){
        String[] parts = name.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            initials.append(part.substring(0,1));
        }
        return initials.toString();
    }

    public Flux<MembersDto> getAllCredentials(){
        log.info("[CredentialsService] getAllCredentials");
        return credentialsRepository.findAll().map(cred ->{
            MembersDto dto = new MembersDto();
            BeanUtils.copyProperties(cred, dto);
            return dto;
        });
    }
    public Mono<MembersDto> performLogin(MembersDto newCredential){
        log.info("[CredentialsService] performLogin");
        return credentialsRepository.findByEmailAndPassword(newCredential.getEmail(), newCredential.getPassword())
                .map(found->{
                    MembersDto dto = new MembersDto();
                    BeanUtils.copyProperties(found, dto);
                    return dto;
                })
                .switchIfEmpty(Mono.error(new CredentialNotFoundException(newCredential.getEmail())));
    }

    public Mono<MembersDto> performUpdateProfilePic(String id, String base64File){
        log.info("[CredentialsService] performUpdateProfilePic");
        return membersRepository.findById(id)
            .flatMap(found->{
                found.setProfilePicture(base64File);
                return membersRepository.save(found).map(savedMember ->{
                    log.info("Profile picture updated successfully");
                    MembersDto dto = new MembersDto();
                    BeanUtils.copyProperties(savedMember, dto);
                    return dto;
                });
            })
            .switchIfEmpty(Mono.error(new CredentialNotFoundException(id)));
    }

}
