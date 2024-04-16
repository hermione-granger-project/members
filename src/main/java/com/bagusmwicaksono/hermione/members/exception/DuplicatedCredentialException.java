package com.bagusmwicaksono.hermione.members.exception;

public class DuplicatedCredentialException extends RuntimeException{
    public DuplicatedCredentialException(String email) {
        super("Duplicate credential with email:"+email);
    }
}
