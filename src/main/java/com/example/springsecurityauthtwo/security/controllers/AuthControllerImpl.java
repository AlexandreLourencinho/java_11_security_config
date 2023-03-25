package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.model.dtos.*;
import com.example.springsecurityauthtwo.security.services.AuthControllerServices;

import java.util.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the authentication, account creation, role management, etc
 *
 * @author Alexandre Lourencinho
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class AuthControllerImpl implements AuthController {

    private AuthControllerServices authControllerServices;

    @Override
    @PostMapping("/public/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return authControllerServices.registerUser(signupRequest);
    }


    @Override
    @PostMapping("/public/signing")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authControllerServices.authenticateUser(loginRequest);
    }


    @Override
    @GetMapping("/public/refreshToken")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        return authControllerServices.refreshToken(request);
    }

    @Override
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserInfoResponse>> getUsersList(HttpServletRequest request) {
        return authControllerServices.getUsersList(request);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request) {
        return authControllerServices.getUser(request);
    }

    @Override
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody SignupRequest userDto, HttpServletRequest request) {
        return authControllerServices.updateUser(userDto, request);
    }

    @Override
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> updateSelectedUser(@PathVariable String userId, @RequestBody SignupRequest updatedUser) {
        return authControllerServices.updateSelectedUser(Long.parseLong(userId), updatedUser);

    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest) {
        return authControllerServices.deleteUser(request, deleteRequest);
    }

    @Override
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteSelectedUser(@PathVariable String userId) {
        return authControllerServices.deleteSelectedUser(Long.parseLong(userId));
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void testController() {
    }


}