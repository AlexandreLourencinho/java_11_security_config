package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.model.dtos.*;
import com.example.springsecurityauthtwo.security.services.AuthControllerServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Controller for the authentification, account creation, role management, etc
 *
 * @author Alexandre Lourencinho
 * @version 1.0
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
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authControllerServices.authenticateUser(loginRequest);
    }


    @Override
    @GetMapping("/public/refreshToken")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        return authControllerServices.refreshToken(request);
    }

    @Override
    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody SignupRequest userDto, HttpServletRequest request) {
        return authControllerServices.updateUser(userDto, request);
    }

    @Override
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateSelectedUser(@PathVariable String userId, @RequestBody SignupRequest updatedUser) {
        return authControllerServices.updateSelectedUser(Long.parseLong(userId), updatedUser);

    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest) {
        return authControllerServices.deleteUser(request, deleteRequest);
    }

    @Override
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteSelectedUser(@PathVariable String userId) {
        return authControllerServices.deleteSelectedUser(Long.parseLong(userId));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public void testController() {
    }


}