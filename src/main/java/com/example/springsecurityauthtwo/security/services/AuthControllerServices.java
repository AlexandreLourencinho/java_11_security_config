package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.dtos.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface AuthControllerServices {

    /**
     * write - save a new user in the database
     *
     * @param signupRequest the object request that will be used to create a user
     * @return to be changed? otherwise : the user's username and roles
     */
    ResponseEntity<Map<String, Object>> registerUser(SignupRequest signupRequest);

    /**
     * read - retrieve user's infos and authenticate him by sending a JWT and a refresh token
     *
     * @param loginRequest username and password for authentication
     * @return TODO to change ? intérêt de renvoyer les infos utilisateurs à ce moment là ?
     */
    ResponseEntity<UserInfoResponse> authenticateUser(LoginRequest loginRequest);

    /**
     * read - get a new jwt token using the refresh token
     *
     * @param request the http servlet request obejct
     * @return a map containing the new JWT token, refreshed
     */
    ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request);

    /**
     * retrieve the list of all users
     *
     * @param request the http servlet request obejct
     * @return a list of user with username ,email and roles
     */
    ResponseEntity<List<UserInfoResponse>> getUsersList(HttpServletRequest request);

    /**
     * retrieve informations of one user
     *
     * @param request the http servlet request obejct
     * @return a user info response pojo containing username, mail and roles
     */
    ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request);

    /**
     * write - update the current user, based on the jwt
     *
     * @param userDto the updated informations of the user
     * @param request the http servlet request obejct
     * @return a confirmation message + a new jwt (with updated claims) or an error message
     */
    ResponseEntity<Map<String, Object>> updateUser(SignupRequest userDto, HttpServletRequest request);

    /**
     * write - update the selected user, need administrator rights
     *
     * @param userId      the user id, type Long
     * @param updatedUser the updated informations of the user
     * @return a confirmation or error message
     */
    ResponseEntity<MessageResponse> updateSelectedUser(Long userId, SignupRequest updatedUser);

    /**
     * write - delete the current user
     *
     * @param request       the http servlet request obejct
     * @param deleteRequest a double confirmation pojo to confirm deletion
     * @return a confirmation or error message
     */
    ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest);

    /**
     * write - delete the selected user
     *
     * @param userId the user id, type Long
     * @return a confirmation or error message
     */
    ResponseEntity<MessageResponse> deleteSelectedUser(Long userId);
}
