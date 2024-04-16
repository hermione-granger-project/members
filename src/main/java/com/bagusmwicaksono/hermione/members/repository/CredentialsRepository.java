package com.bagusmwicaksono.hermione.members.repository;

import com.bagusmwicaksono.hermione.members.model.Credentials;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CredentialsRepository extends ReactiveMongoRepository<Credentials, String> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Credentials> findByEmailAndPassword(String email, String password);
}
