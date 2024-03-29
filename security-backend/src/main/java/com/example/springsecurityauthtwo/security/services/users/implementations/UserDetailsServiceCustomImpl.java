package com.example.springsecurityauthtwo.security.services.users.implementations;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsServicesCustom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * implementation of the loadUserByUsername overridden method
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@AllArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceCustomImpl implements UserDetailsServicesCustom {

    private final AppUserRepository userRepository;

    /**
     * get UserDetails instance from a username
     *
     * @param username the username identifying the user whose data is required.
     * @return an instance of UserDetailsCustom
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    @Transactional
    public UserDetailsCustom loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loading userDetails by username...");
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDetailsCustomImpl.build(user);
    }

}
