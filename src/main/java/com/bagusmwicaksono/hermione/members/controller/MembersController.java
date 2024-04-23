package com.bagusmwicaksono.hermione.members.controller;

import com.bagusmwicaksono.hermione.members.controller.dto.MembersDto;
import com.bagusmwicaksono.hermione.members.service.CredentialsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/members")
@Slf4j
public class MembersController {
    private final CredentialsService credentialsService;

    public MembersController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping("")
    @CrossOrigin(origins = "*")
    public Mono<MembersDto> createCred(@RequestBody MembersDto credDto){
        log.info("[CredentialsController] createCred updated credDto="+credDto.toString());
        return credentialsService.performCreateCredential(credDto);
    }

    @GetMapping("")
    @CrossOrigin(origins = "*")
    public Flux<MembersDto> getAllCred() {
        log.info("[CredentialsController] getAllCred");
        return credentialsService.getAllCredentials();
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public Mono<MembersDto> performLogin(@RequestBody MembersDto credDto){
        log.info("[CredentialsController] performLogin credDto="+credDto.toString());
        return credentialsService.performLogin(credDto);
    }

    @PutMapping("/profilepic/{id}")
@CrossOrigin(origins = "*")
public Mono<MembersDto> updateProfilePic(@PathVariable("id") String id, @RequestBody String fileString){
    log.info("[CredentialsController] updateProfilePic id:"+id);
    return credentialsService.performUpdateProfilePic(id, fileString);
}
}
