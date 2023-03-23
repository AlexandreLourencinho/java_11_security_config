package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserUpdateError {

    private String usernameTaken;
    private String emailTaken;

}
