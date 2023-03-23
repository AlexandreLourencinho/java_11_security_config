package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.repositories.AppRoleRepository;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.exceptions.RoleNotFoundException;
import com.example.springsecurityauthtwo.security.exceptions.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * The class UserServicesImpl which implements the interface UserServices.
 *
 * @author Alexandre
 * @version 1.1
 * @since 1.0
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserServicesImpl implements UserServices {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser findUserByUsername(String username) {
        log.info("retrieving AppUser from his username...");
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Boolean usernameAlreadyExists(String username) {
        log.info("checking if username is already taken...");
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean emailAlreadyExists(String email) {
        log.info("checking if email is already taken...");
        return userRepository.existsByEmail(email);
    }

    @Override
    public AppRole findRoleByRolename(ERole rolename) {
        log.info("retrieving the role...");
        return roleRepository.findByName(rolename).orElse(null);
    }

    @Override
    public AppUser saveNewUser(AppUser user) {
        log.info("registering a new user...");
        return userRepository.save(user);
    }

    @Override
    public void manageRoles(Set<String> strRoles, Set<AppRole> roles) {
        log.info("managing new user's roles...");
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    AppRole adminRole = findRoleByRolename(ERole.ROLE_ADMIN);
                    if (adminRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(adminRole);
                    break;
                case "mod":
                    AppRole modRole = findRoleByRolename(ERole.ROLE_MODERATOR);
                    if (modRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(modRole);
                    break;
                default:
                    AppRole userRole = findRoleByRolename(ERole.ROLE_USER);
                    if (userRole == null) {
                        throw new RoleNotFoundException(SecurityConstants.ERROR_ROLE);
                    }
                    roles.add(userRole);
                    break;
            }
        });
    }

    @Override
    public AppUser findById(Long id) {
        log.info("finding a user through it's id....");
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<AppUser> findAll() {
        log.info("retrieving list of all users...");
        Iterable<AppUser> userIt = userRepository.findAll();
        List<AppUser> listUser = new ArrayList<>();
        userIt.forEach(listUser::add);
        return listUser;
    }

    @Override
    public AppUser updateUserInfo(SignupRequest user, String username) {
        AppUser oldUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User", "username", username));
        Set<AppRole> enumRoles;
        log.warn("user : {}", user);
        if (Objects.nonNull(user.getRoles())) {
            enumRoles = user.getRoles().stream()
                    .map(role -> roleRepository.findByName(ERole.valueOf(role))
                            .orElseThrow(() -> new UserNotFoundException("Role", "name", role)))
                    .collect(Collectors.toSet());
        } else {
            enumRoles = oldUser.getRoles();
        }

        if (!oldUser.getUsername().equals(user.getUsername()) && Boolean.FALSE.equals(usernameAlreadyExists(user.getUsername()))) {
            oldUser.setUsername(user.getUsername());
        }
        if (!oldUser.getEmail().equals(user.getEmail()) && Boolean.FALSE.equals(emailAlreadyExists(user.getEmail()))) {
            oldUser.setEmail(user.getEmail());
        }
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()))
                .setRoles(enumRoles);
        return userRepository.save(oldUser);
    }

    @Override
    public void deleteUser(AppUser user) {
        log.info("deleting user" + user.getUsername() + "....");
        userRepository.delete(user);
    }

}
