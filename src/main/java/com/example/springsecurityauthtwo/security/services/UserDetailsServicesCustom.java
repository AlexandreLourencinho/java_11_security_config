package com.example.springsecurityauthtwo.security.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * the custom interface for UserDetailsService, allow custom return type for og interface methods
 *
 * @author Lourencinho Alexandre
 * @version 1.0.0
 */
public interface UserDetailsServicesCustom extends UserDetailsService {

    UserDetailsCustom loadUserByUsername(String username) throws UsernameNotFoundException;
}
