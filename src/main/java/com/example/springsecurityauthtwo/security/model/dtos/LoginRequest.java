package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Login request model object
 */
@Data
@Accessors(chain = true)
public class LoginRequest {

    private String username;
    private String password;
}
