package com.example.springsecurityauthtwo.security.exceptions;

/**
 * Exception thrown when a jwt token error occurred
 */
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }

}
