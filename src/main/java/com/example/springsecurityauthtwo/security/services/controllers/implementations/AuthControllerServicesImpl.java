package com.example.springsecurityauthtwo.security.services.controllers.implementations;

import com.example.springsecurityauthtwo.security.jwt.interfaces.JwtUtils;
import com.example.springsecurityauthtwo.security.model.dtos.*;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.example.springsecurityauthtwo.security.model.mappers.AppUserMapper;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserServices;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsServicesCustom;
import com.example.springsecurityauthtwo.security.services.users.implementations.UserDetailsCustomImpl;
import com.example.springsecurityauthtwo.security.services.controllers.interfaces.AuthControllerServices;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
@Service
public class AuthControllerServicesImpl implements AuthControllerServices {

    private JwtUtils jwtUtils;
    private UserServices userServices;
    private AuthenticationManager manager;
    private PasswordEncoder passwordEncoder;
    private UserDetailsServicesCustom userDetailsService;

    @Override
    public ResponseEntity<Map<String, Object>> registerUser(SignupRequest signupRequest) {

        log.warn("registering a new user...");
        Map<String, Object> responseBody = new HashMap<>();

        // check if username already taken
        if (Boolean.TRUE.equals(userServices.usernameAlreadyExists(signupRequest.getUsername()))) {
            log.warn(SecurityConstants.ERROR_USERNAME_TAKEN);
            responseBody.put(SecurityConstants.ERROR, SecurityConstants.ERROR_USERNAME_TAKEN);
            return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }

        // check if email already taken
        if (Boolean.TRUE.equals(userServices.emailAlreadyExists(signupRequest.getEmail()))) {
            log.warn(SecurityConstants.ERROR_MAIL_TAKEN);
            responseBody.put(SecurityConstants.ERROR, SecurityConstants.ERROR_MAIL_TAKEN);
            return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }

        AppUser user = new AppUser()
                .setUsername(signupRequest.getUsername())
                .setEmail(signupRequest.getEmail())
                .setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // check user's role
        Set<AppRole> roles = userServices.getAppRoles(signupRequest);

        user.setRoles(roles);
        AppUser registeredUser = userServices.saveNewUser(user);
        log.info("user registered successfully!");
        responseBody.put(SecurityConstants.SUCCESS, String.format("User %s registered successfully", registeredUser.getUsername()));

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseBody);
    }

    @Override
    public ResponseEntity<Map<String, Object>> authenticateUser(LoginRequest loginRequest) {
        // context and auth management
        Map<String, Object> responseBody = new HashMap<>();
        log.info("User signing in...");
        Authentication auth = manager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsCustom userDetails = (UserDetailsCustomImpl) auth.getPrincipal();

        // jwt generation
        Map<String, String> tokens = jwtUtils.generateTokenAndRefresh(userDetails.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_TOKEN, SecurityConstants.TOKEN_START + tokens.get("token"));
        headers.add(SecurityConstants.REFRESH_TOKEN, SecurityConstants.TOKEN_START_REFRESH + tokens.get("refreshToken"));
        UserInfoResponse resp = AppUserMapper.INSTANCE.userDetailsToUserInfoResponse(userDetails);
        responseBody.put("user", resp);
        log.info("user signed in");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseBody);
    }

    @Override
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();

        String refreshToken = jwtUtils.getRefreshTokenFromHeaders(request);

        if (StringUtils.isEmpty(refreshToken)) {
            responseBody.put(SecurityConstants.ERROR, "no refresh token provided");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }

        String username = jwtUtils.getUsernameFromToken(refreshToken);
        UserDetailsCustom user = userDetailsService.loadUserByUsername(username);

        if (Boolean.FALSE.equals(jwtUtils.validateToken(refreshToken, user))) {
            responseBody.put(SecurityConstants.ERROR, SecurityConstants.INVALID_REFRESH);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }

        String newAccessToken = jwtUtils.generateJwtToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.HEADER_TOKEN, newAccessToken);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).contentType(MediaType.APPLICATION_JSON).body(responseBody);
    }

    @Override
    public ResponseEntity<List<UserInfoResponse>> getUsersList(HttpServletRequest request) {
        List<AppUser> listUser = userServices.findAll();


        if (listUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            List<UserInfoResponse> listInfoResponse = new ArrayList<>();
            listUser.forEach(user -> {
                UserInfoResponse returnedUser = AppUserMapper.INSTANCE.appUserToUserInfoResponse(user);
                listInfoResponse.add(returnedUser);
            });

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(listInfoResponse);
        }

    }

    @Override
    public ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request) {
        final String token = request.getHeader(SecurityConstants.HEADER_TOKEN).substring(SecurityConstants.BEARER_SUBSTRING);
        final String username = jwtUtils.getUsernameFromToken(token);
        AppUser user = userServices.findUserByUsername(username);

        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            UserInfoResponse returnedUser = AppUserMapper.INSTANCE.appUserToUserInfoResponse(user);

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(returnedUser);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateUser(SignupRequest userDto, HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        UserUpdateError errors = new UserUpdateError();
        String username = jwtUtils.getUsernameFromToken(jwtUtils.getTokenFromHeaders(request));

        ResponseEntity<Map<String, Object>> isConflictResponse = checkAlreadyTakenErrors(userDto, responseBody, errors);
        if (isConflictResponse != null) return isConflictResponse;

        AppUser updatedUser = userServices.updateUserInfo(userDto, username);

        if (Objects.isNull(updatedUser)) {
            log.info("fail to update user");
            responseBody.put(SecurityConstants.ERROR, "Could not update user.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        } else {
            String newToken = jwtUtils.generateJwtToken(userDto.getUsername());
            UserInfoResponse returnedUser = AppUserMapper.INSTANCE.appUserToUserInfoResponse(updatedUser);
            HttpHeaders headers = new HttpHeaders();
            headers.set(SecurityConstants.HEADER_TOKEN, newToken);
            responseBody.put("user", returnedUser);
            log.info("user updated successfully");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseBody);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateSelectedUser(Long userId, SignupRequest updatedUser) {
        Map<String, Object> responseBody = new HashMap<>();
        UserUpdateError errors = new UserUpdateError();

        ResponseEntity<Map<String, Object>> isConflictResponse = checkAlreadyTakenErrors(updatedUser, responseBody, errors);
        if (isConflictResponse != null) return isConflictResponse;

        AppUser user = userServices.findById(userId);

        if (Objects.isNull(user)) {
            responseBody.put(SecurityConstants.ERROR, "could not update user : user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

        } else {
            String username = user.getUsername();
            AppUser updatedUserReturn = userServices.updateUserInfo(updatedUser, username);

            if (Objects.isNull(updatedUserReturn)) {
                responseBody.put(SecurityConstants.ERROR, "something went wrong when updating the user: please contact an administrator.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);

            } else {
                responseBody.put(SecurityConstants.SUCCESS, String.format("user %s updated successfully", username));
                UserInfoResponse returnedUser = AppUserMapper.INSTANCE.appUserToUserInfoResponse(user);
                log.warn("mapped return user : {}", returnedUser);
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            }
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> checkAlreadyTakenErrors(SignupRequest userDto, Map<String, Object> responseBody, UserUpdateError errors) {
        Boolean isMailTaken = userServices.emailAlreadyExists(userDto.getEmail());
        Boolean isUsernameTaken = userServices.usernameAlreadyExists(userDto.getUsername());

        if (Boolean.TRUE.equals(isMailTaken)) {
            errors.setUsernameTaken(SecurityConstants.ERROR_USERNAME_TAKEN);
        }
        if (Boolean.TRUE.equals(isUsernameTaken)) {
            errors.setEmailTaken(SecurityConstants.ERROR_MAIL_TAKEN);
        }
        if (isUsernameTaken || isMailTaken) {
            responseBody.put(SecurityConstants.ERROR, errors);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }
        return null;
    }

    @Override
    public ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest) {
        MessageResponse response = new MessageResponse();

        if (!deleteRequest.isDeleteRequest() || !deleteRequest.isConfirmedDeleteRequest()) {
            response.setMessage("your account hasn't been deleted: missing confirmation");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else {
            String username = jwtUtils.getUsernameFromToken(jwtUtils.getTokenFromHeaders(request));
            AppUser user = this.userServices.findUserByUsername(username);
            userServices.deleteUser(user);
            response.setMessage("your account have been correctly deleted");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteSelectedUser(Long userId) {

        MessageResponse response = new MessageResponse();
        AppUser user = userServices.findById(userId);

        if (Objects.isNull(user)) {
            response.setMessage(SecurityConstants.USER_NOT_FOUND);
            log.error(SecurityConstants.USER_NOT_FOUND);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            userServices.deleteUser(user);
            response.setMessage(String.format("User %s deleted successfully", user.getUsername()));

            return ResponseEntity.ok(response);
        }
    }

}
