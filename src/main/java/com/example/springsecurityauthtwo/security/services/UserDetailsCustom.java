package com.example.springsecurityauthtwo.security.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * TODO voir à remplacer totalement UserDetails interface par celle ci
 */
public interface UserDetailsCustom extends UserDetails {

    /**
     *
     * @return
     */
    String getEmail();
}
