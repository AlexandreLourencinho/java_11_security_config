package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.model.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

public interface AuthController {

    /**
     * Read - Path allowing user to connect
     * @param loginRequest the login request that contains username and password
     * @return users infos with access and refresh token
     */
    ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    /**
     * Write - Path allowing a user to create an account
     * @param signupRequest Object that contains username mail password and roles. default role is user
     * @return A string that signify if the user is created or not
     */
    ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody SignupRequest signupRequest);

    /**
     * read - generate a new access token using the refresh token
     * @param request the http servlet request object
     * @return a http response with the new token or the error
     */
    ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request);

    /**
     * write - update an existing user
     * @param userDto the user info to be updated
     * @param request the HttpServlet Request
     * @return the modified user or an error
     */
    ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody SignupRequest userDto, HttpServletRequest request);
    ResponseEntity<MessageResponse> updateSelectedUser(@PathVariable String userId, @RequestBody SignupRequest updatedUser);
    ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest);
    ResponseEntity<MessageResponse> deleteSelectedUser(@PathVariable String userId);
}
