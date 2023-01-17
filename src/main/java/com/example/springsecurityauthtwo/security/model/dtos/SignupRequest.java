package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    private String username;
    private String email;
    private String password;
    private Set<String> roles;

}
