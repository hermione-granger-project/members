package com.bagusmwicaksono.hermione.members.controller;

import com.bagusmwicaksono.hermione.members.controller.dto.CredentialsDto;
import com.bagusmwicaksono.hermione.members.service.CredentialsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public Mono<CredentialsDto> createCred(@RequestBody CredentialsDto credDto){
        log.info("[CredentialsController] createCred updated credDto="+credDto.toString());
        return credentialsService.performCreateCredential(credDto);
    }

    @GetMapping("")
    @CrossOrigin(origins = "*")
    public Flux<CredentialsDto> getAllCred() {
        log.info("[CredentialsController] getAllCred");
        return credentialsService.getAllCredentials();
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public Mono<CredentialsDto> performLogin(@RequestBody CredentialsDto credDto){
        log.info("[CredentialsController] performLogin credDto="+credDto.toString());
        return credentialsService.performLogin(credDto);
    }
}
