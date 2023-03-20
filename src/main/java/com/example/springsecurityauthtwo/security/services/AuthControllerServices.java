package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.dtos.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthControllerServices {

    ResponseEntity<Map<String, Object>> registerUser(SignupRequest signupRequest);
    ResponseEntity<UserInfoResponse> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request);
    ResponseEntity<Map<String, Object>> updateUser(SignupRequest userDto, HttpServletRequest request);
    ResponseEntity<MessageResponse> updateSelectedUser(Long userId, SignupRequest updatedUser);
    ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest);
    ResponseEntity<MessageResponse> deleteSelectedUser(Long userId);
}
