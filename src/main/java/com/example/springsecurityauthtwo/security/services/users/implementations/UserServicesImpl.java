package com.example.springsecurityauthtwo.security.services.users.implementations;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.repositories.AppRoleRepository;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.exceptions.RoleNotFoundException;
import com.example.springsecurityauthtwo.security.exceptions.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import com.example.springsecurityauthtwo.security.services.users.interfaces.UserServices;
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
    public AppUser saveNewUser(AppUser user) {
        log.info("registering a new user...");
        return userRepository.save(user);
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
            enumRoles = getAppRoles(user);
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
    public Set<AppRole> getAppRoles(SignupRequest user) {
        Set<AppRole> enumRoles;
        if(Objects.isNull(user.getRoles())) {
            enumRoles = new HashSet<>();
            enumRoles.add(roleRepository.findByName(ERole.ROLE_USER.getValue()).orElse(null));
        } else {
            enumRoles = user.getRoles().stream()
                    .map(role -> {
                        ERole enumRole = ERole.fromString(role);
                        if(Objects.nonNull(enumRole)) {
                            return roleRepository.findByName(enumRole.getValue())
                                    .orElseThrow(() -> new RoleNotFoundException("Role", "name", role));
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if(enumRoles.isEmpty()) {
                enumRoles.add(roleRepository.findByName(ERole.ROLE_USER.getValue()).orElse(null));
            }
        }
        return enumRoles;
    }

    @Override
    public void deleteUser(AppUser user) {
        log.info("deleting user" + user.getUsername() + "....");
        userRepository.delete(user);
    }

}
