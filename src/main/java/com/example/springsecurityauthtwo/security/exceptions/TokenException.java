package com.example.springsecurityauthtwo.security.exceptions;

public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }

}
