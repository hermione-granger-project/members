package com.bagusmwicaksono.hermione.members.controller;

import com.bagusmwicaksono.hermione.members.controller.dto.CredentialsDto;
import com.bagusmwicaksono.hermione.members.exception.CredentialNotFoundException;
import com.bagusmwicaksono.hermione.members.exception.DuplicatedCredentialException;
import com.bagusmwicaksono.hermione.members.service.CredentialsService;
import com.bagusmwicaksono.hermione.members.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(MembersController.class)
class MembersControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CredentialsService credentialsService;

    @Test
    void testCreateCred_WhenSuccess_ShouldReturnValid() throws BeansException, IOException {
        CredentialsDto credentialsDto = TestUtils.getCredentialDtoTestData();
        when(credentialsService.performCreateCredential(any())).thenReturn(Mono.just(credentialsDto));

        webTestClient.post().uri("/v1/members")
                .bodyValue(credentialsDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CredentialsDto.class);
    }

    @Test
    void testCreateCred_WhenDuplicated_ShouldReturnError() throws BeansException, IOException {
        CredentialsDto credentialsDto = TestUtils.getCredentialDtoTestData();
        when(credentialsService.performCreateCredential(any())).thenThrow(new DuplicatedCredentialException("dummy@email"));

        webTestClient.post().uri("/v1/members")
                .bodyValue(credentialsDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testGetAllCred_WhenSuccess_ShouldReturnValid() throws BeansException, IOException {
        CredentialsDto credentialsDto = TestUtils.getCredentialDtoTestData();

        when(credentialsService.getAllCredentials()).thenReturn(Flux.just(credentialsDto));

        webTestClient.get().uri("/v1/members")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CredentialsDto.class);
    }

    @Test
    void performLogin_WhenSuccess_ShouldReturnValid() throws BeansException, IOException {
        CredentialsDto credentialsDto = TestUtils.getCredentialDtoTestData();

        when(credentialsService.performLogin(any())).thenReturn(Mono.just(credentialsDto));

        webTestClient.post().uri("/v1/members/login")
                .bodyValue(credentialsDto)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CredentialsDto.class);
    }

    @Test
    void performLogin_WheNotFound_ShouldReturnError() throws BeansException, IOException {
        CredentialsDto credentialsDto = TestUtils.getCredentialDtoTestData();

        when(credentialsService.performLogin(any())).thenThrow(new CredentialNotFoundException(credentialsDto.getEmail()));

        webTestClient.post().uri("/v1/members/login")
                .bodyValue(credentialsDto)
                .exchange()
                .expectStatus().isNotFound();
    }
}