package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.TokenUsed;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.repositories.TokenUsedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * implementation of the loadUserByUsername overridden method
 */
@AllArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    /**
     * get UserDetails instance from a username
     * @param username the username identifying the user whose data is required.
     * @return an instance of UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loading userDetails by username...");
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDetailsImpl.build(user);
    }

}