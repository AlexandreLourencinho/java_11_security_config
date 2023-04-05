package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * Login request model object
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
