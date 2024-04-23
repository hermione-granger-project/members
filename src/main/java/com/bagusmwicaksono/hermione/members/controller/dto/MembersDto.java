package com.bagusmwicaksono.hermione.members.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembersDto {
    private String id;
    private String credId;
    private String email;
    private String username;
    private String password;
}