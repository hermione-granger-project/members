package com.bagusmwicaksono.hermione.members.service;

import com.bagusmwicaksono.hermione.members.controller.dto.MembersDto;
import com.bagusmwicaksono.hermione.members.exception.DuplicatedCredentialException;
import com.bagusmwicaksono.hermione.members.model.Credentials;
import com.bagusmwicaksono.hermione.members.repository.CredentialsRepository;
import com.bagusmwicaksono.hermione.members.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CredentialsServiceTest {
    @InjectMocks
    CredentialsService credentialsService;

    @Mock
    CredentialsRepository credentialsRepository;

    @Test
    void testPerformCreateCredential_whenSuccess_returnValidResponse() throws IOException {
        Credentials credentials = TestUtils.getCredentialTestData();

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(credentialsRepository.save(any())).thenReturn(Mono.just(credentials));

        MembersDto newCredentialsDto = new MembersDto();
        BeanUtils.copyProperties(credentials, newCredentialsDto);
        Mono<MembersDto> resultDto = credentialsService.performCreateCredential(newCredentialsDto);

        StepVerifier.create(resultDto).consumeNextWith(newCred -> {
            assertEquals(newCred.getId(), credentials.getId());
            assertEquals(newCred.getEmail(), credentials.getEmail());
            assertEquals(newCred.getUsername(), credentials.getUsername());
        }).verifyComplete();
    }
    @Test
    void testPerformCreateCredential_whenDuplicateEmail_returnDuplicateError() throws IOException {
        Credentials credentials = TestUtils.getCredentialTestData();

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(Mono.just(true));

        MembersDto newCredentialsDto = new MembersDto();
        BeanUtils.copyProperties(credentials, newCredentialsDto);
        Mono<MembersDto> resultDto = credentialsService.performCreateCredential(newCredentialsDto);

        StepVerifier.create(resultDto)
                .expectErrorMatches(throwable -> throwable instanceof DuplicatedCredentialException)
                .verify();
    }
    @Test
    void getAllCredentials_whenSuccess_returnAll() throws IOException {
        Credentials credentials = TestUtils.getCredentialTestData();
        when(credentialsRepository.findAll()).thenReturn(Flux.just(credentials));

        Flux<MembersDto> allCredentials = credentialsService.getAllCredentials();

        StepVerifier.create(allCredentials).expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void performLogin_whenSuccess_returnValidResponse() throws IOException{
        Credentials credentials = TestUtils.getCredentialTestData();

        when(credentialsRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(Mono.just(credentials));

        MembersDto cred = new MembersDto();
        BeanUtils.copyProperties(credentials, cred);

        Mono<MembersDto> resultDto = credentialsService.performLogin(cred);

        StepVerifier.create(resultDto).consumeNextWith(foundCred ->{
            assertEquals(foundCred.getEmail(), credentials.getEmail());
            assertEquals(foundCred.getPassword(), credentials.getPassword());
            assertEquals(foundCred.getId(), credentials.getId());
        }).verifyComplete();

    }

    @Test
    void performLogin_whenNotFound_returnNotfoundError() throws IOException{
        Credentials credentials = TestUtils.getCredentialTestData();

        when(credentialsRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(Mono.empty());

        MembersDto cred = new MembersDto();
        BeanUtils.copyProperties(credentials, cred);

        Mono<MembersDto> resultDto = credentialsService.performLogin(cred);

        StepVerifier.create(resultDto)
                .expectError()
                .verify();
    }
}