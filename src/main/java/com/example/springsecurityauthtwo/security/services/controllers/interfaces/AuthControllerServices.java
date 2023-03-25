package com.example.springsecurityauthtwo.security.services.controllers.interfaces;

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
     * @return the saved user in the user key, or an error in the error key
     */
    ResponseEntity<Map<String, Object>> registerUser(SignupRequest signupRequest);

    /**
     * read - retrieve user's infos and authenticate him by sending a JWT and a refresh token
     *
     * @param loginRequest username and password for authentication
     * @return the authenticated user in the user key, or an error in the error key
     */
    ResponseEntity<Map<String, Object>> authenticateUser(LoginRequest loginRequest);

    /**
     * read - get a new jwt token using the refresh token
     *
     * @param request the http servlet request object
     * @return a map containing the new JWT token, refreshed
     */
    ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request); // TODO voir le retour de cette méthode

    /**
     * retrieve the list of all users
     *
     * @param request the http servlet request object
     * @return a list of user with username ,email and roles
     */
    ResponseEntity<List<UserInfoResponse>> getUsersList(HttpServletRequest request);

    /**
     * retrieve information of one user
     *
     * @param request the http servlet request object
     * @return a user info response pojo containing username, mail and roles
     */
    ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request);

    /**
     * write - update the current user, based on the jwt
     *
     * @param userDto the updated information of the user
     * @param request the http servlet request object
     * @return a confirmation message + a new jwt (with updated claims) or an error message
     */
    ResponseEntity<Map<String, Object>> updateUser(SignupRequest userDto, HttpServletRequest request);

    /**
     * write - update the selected user, need administrator rights
     *
     * @param userId      the user id, type Long
     * @param updatedUser the updated information of the user
     * @return a confirmation or error message
     */
    ResponseEntity<Map<String, Object>> updateSelectedUser(Long userId, SignupRequest updatedUser);

    /**
     * check if username or email is already taken
     *
     * @param userDto      the user DTO object
     * @param responseBody the body of the response that will be sent
     * @param errors       the UserUpdateError POJO
     * @return a map with the errors, or null if none is present
     * @see UserUpdateError
     */
    ResponseEntity<Map<String, Object>> checkAlreadyTakenErrors(SignupRequest userDto, Map<String, Object> responseBody, UserUpdateError errors);

    /**
     * write - delete the current user
     *
     * @param request       the http servlet request object
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
