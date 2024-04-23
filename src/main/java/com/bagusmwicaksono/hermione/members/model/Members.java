package com.bagusmwicaksono.hermione.members.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "members")
public class Members {
    @Id
    private String id;
    private String fullname;
    private String initial;
    private String profilePicture;
    private String status;
}
