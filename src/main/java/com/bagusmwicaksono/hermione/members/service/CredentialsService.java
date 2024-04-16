package com.bagusmwicaksono.hermione.members.service;

import com.bagusmwicaksono.hermione.members.controller.dto.CredentialsDto;
import com.bagusmwicaksono.hermione.members.exception.CredentialNotFoundException;
import com.bagusmwicaksono.hermione.members.exception.DuplicatedCredentialException;
import com.bagusmwicaksono.hermione.members.model.Credentials;
import com.bagusmwicaksono.hermione.members.repository.CredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;

    public CredentialsService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }
    public Mono<CredentialsDto> performCreateCredential(CredentialsDto newCredential){
        log.info("[CredentialsService] performCreateCredential");
        return credentialsRepository.existsByEmail(newCredential.getEmail()).flatMap(result->{
            if(Boolean.TRUE.equals(result)){
                return Mono.error(new DuplicatedCredentialException(newCredential.getEmail()));
            }
            Credentials credentials = new Credentials();
            BeanUtils.copyProperties(newCredential, credentials);
            return credentialsRepository.save(credentials).map(saveCred ->{
                CredentialsDto credentialsDto = new CredentialsDto();
                BeanUtils.copyProperties(saveCred, credentialsDto);
                return credentialsDto;
            });
        });
    }
    public Flux<CredentialsDto> getAllCredentials(){
        log.info("[CredentialsService] getAllCredentials");
        return credentialsRepository.findAll().map(cred ->{
            CredentialsDto dto = new CredentialsDto();
            BeanUtils.copyProperties(cred, dto);
            return dto;
        });
    }
    public Mono<CredentialsDto> performLogin(CredentialsDto newCredential){
        log.info("[CredentialsService] performLogin");
        return credentialsRepository.findByEmailAndPassword(newCredential.getEmail(), newCredential.getPassword())
                .map(found->{
                    CredentialsDto dto = new CredentialsDto();
                    BeanUtils.copyProperties(found, dto);
                    return dto;
                })
                .switchIfEmpty(Mono.error(new CredentialNotFoundException(newCredential.getEmail())));
    }
}
