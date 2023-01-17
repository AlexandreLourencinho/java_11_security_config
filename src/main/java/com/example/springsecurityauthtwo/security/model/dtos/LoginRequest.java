package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
