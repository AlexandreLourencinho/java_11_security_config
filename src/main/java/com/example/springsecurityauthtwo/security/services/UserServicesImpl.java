package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.exceptions.RoleNotFoundException;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.repositories.AppRoleRepository;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserServicesImpl implements UserServices {
    // TODO gerer les tokens incrementation ici

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;

    /**
     *
     * @param username
     * @return
     */
    @Override
    public AppUser findUserByUsername(String username) {
        log.info("retievring AppUser from his username...");
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public Boolean usernameAlreadyExists(String username) {
        log.info("checking if username is already taken...");
        return userRepository.existsByUsername(username);
    }

    /**
     *
     * @param email
     * @return
     */
    @Override
    public Boolean emailAlreadyExists(String email) {
        log.info("checking if email is already taken...");
        return userRepository.existsByEmail(email);
    }

    /**
     *
     * @param rolename
     * @return
     */
    @Override
    public AppRole findRoleByRolename(ERole rolename) {
        log.info("retrieving the role...");
        return roleRepository.findByName(rolename).orElse(null);
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public AppUser saveNewUser(AppUser user) {
        log.info("registering a new user...");
        return userRepository.save(user);
    }

    /**
     *
     * @param strRoles
     * @param roles
     */
    @Override
    public void manageRoles(Set<String> strRoles, Set<AppRole> roles) {
        log.info("managing new user's roles...");
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    AppRole adminRole = findRoleByRolename(ERole.ROLE_ADMIN);
                    if(adminRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(adminRole);
                    break;
                case "mod":
                    AppRole modRole = findRoleByRolename(ERole.ROLE_MODERATOR);
                    if(modRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(modRole);
                    break;
                default:
                    AppRole userRole = findRoleByRolename(ERole.ROLE_USER);
                    if(userRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(userRole);
                    break;
            }
        });
    }

}