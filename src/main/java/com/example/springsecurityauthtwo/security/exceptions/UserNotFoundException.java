package com.example.springsecurityauthtwo.security.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
