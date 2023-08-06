package com.example.springsecurityauthtwo.security.controllers.interfaces;

import com.example.springsecurityauthtwo.security.model.dtos.*;

import java.util.Map;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for the user management paths
 *
 * @author Lourencinho Alexandre
 * @version 1.0.0
 */
public interface AuthController {

    /**
     * Read - Path allowing user to connect
     *
     * @param loginRequest the {@link LoginRequest} that contains username and password
     * @return users infos with access and refresh token
     */
    @PostMapping("/public/signup")
    ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    /**
     * Write - Path allowing a user to create an account
     *
     * @param signupRequest Object that contains username mail password and roles. default role is user
     * @return A string that signify if the user is created or not
     */
    @PostMapping("/public/signing")
    ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody SignupRequest signupRequest);

    /**
     * read - generate a new access token using the refresh token
     *
     * @param request the http servlet request object
     * @return a http response with the new token or the error
     */
    @GetMapping("/public/refreshToken")
    ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request);

    /**
     * read - get a list of all users
     *
     * @param request the http servlet request object
     * @return a list of user info POJOs
     */
    @GetMapping("/list")
    ResponseEntity<List<UserInfoResponse>> getUsersList(HttpServletRequest request);

    /**
     * read - get one user information
     *
     * @param request the http servlet request object
     * @return a user info response pojo
     */
    @GetMapping("")
    ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request);

    /**
     * write - update an existing user
     *
     * @param userDto the user info to be updated
     * @param request the HttpServlet Request
     * @return the modified user or an error
     */
    @PutMapping("/update")
    ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody SignupRequest userDto, HttpServletRequest request);

    /**
     * write - update a selected user from an admin
     *
     * @param userId      the Long user id
     * @param updatedUser the updated information request
     * @return a message stating that if the update occurred or not
     */
    @PutMapping("/update/{userId}")
    ResponseEntity<Map<String, Object>> updateSelectedUser(@PathVariable String userId, @RequestBody SignupRequest updatedUser);

    /**
     * write - delete own user's account
     *
     * @param request       the http servlet request object
     * @param deleteRequest a double check confirmation for the delete request
     * @return a message stating that if the deletion occurred or not
     */
    @DeleteMapping("/delete")
    ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest);

    /**
     * write - delete a user account from an admin
     *
     * @param userId the Long user id
     * @return a message stating that if the deletion occurred or not
     */
    @DeleteMapping("/delete/{userId}")
    ResponseEntity<MessageResponse> deleteSelectedUser(@PathVariable String userId);
}
