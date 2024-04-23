package com.bagusmwicaksono.hermione.members.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bagusmwicaksono.hermione.members.model.Members;

public interface MembersRepository extends ReactiveMongoRepository <Members, String> {
    
}
