package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private String username;
    private List<String> roles;


}
