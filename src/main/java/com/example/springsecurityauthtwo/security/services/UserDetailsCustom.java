package com.example.springsecurityauthtwo.security.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom interface base on UserDetails, allows custom methods implementations
 *
 * @author Lourencinho Alexandre
 * @version 1.0.0
 */
public interface UserDetailsCustom extends UserDetails {

    /**
     * permits the return of the mail in the UserDetails implementations
     *
     * @return the email string
     */
    String getEmail();
}
