package com.example.springsecurityauthtwo.security.services;


import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;

import java.util.Set;

public interface UserServices {

    AppUser findUserByUsername(String username);

    Boolean usernameAlreadyExists(String username);

    Boolean emailAlreadyExists(String email);

    AppRole findRoleByRolename(ERole rolename);

    AppUser saveNewUser(AppUser user);

    void manageRoles(Set<String> strRoles, Set<AppRole> roles);

}
