package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * User info that will be sent after a successful login request
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoResponse {

    private String username;
    private List<String> roles;


}
