package com.bagusmwicaksono.hermione.members.utils;

import com.bagusmwicaksono.hermione.members.controller.dto.CredentialsDto;
import com.bagusmwicaksono.hermione.members.model.Credentials;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static Credentials getCredentialTestData() throws StreamReadException, DatabindException, IOException {
        InputStream inJson = Credentials.class.getResourceAsStream("/Credentials.json");
        return new ObjectMapper().readValue(inJson, Credentials.class);
    }
    public static CredentialsDto getCredentialDtoTestData() throws StreamReadException, DatabindException, BeansException, IOException{
        CredentialsDto credentialsDto = new CredentialsDto();
        BeanUtils.copyProperties(getCredentialTestData(), credentialsDto);
        return credentialsDto;
    }
}
