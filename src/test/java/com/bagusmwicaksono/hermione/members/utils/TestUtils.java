package com.bagusmwicaksono.hermione.members.utils;

import com.bagusmwicaksono.hermione.members.controller.dto.MembersDto;
import com.bagusmwicaksono.hermione.members.model.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static Credentials getCredentialTestData() throws IOException {
        InputStream inJson = Credentials.class.getResourceAsStream("/Credentials.json");
        return new ObjectMapper().readValue(inJson, Credentials.class);
    }
    public static MembersDto getCredentialDtoTestData() throws BeansException, IOException{
        MembersDto credentialsDto = new MembersDto();
        BeanUtils.copyProperties(getCredentialTestData(), credentialsDto);
        return credentialsDto;
    }
}
