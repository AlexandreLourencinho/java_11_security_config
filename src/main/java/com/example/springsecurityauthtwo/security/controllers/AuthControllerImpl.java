package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.jwt.JwtUtils;
import com.example.springsecurityauthtwo.security.model.dtos.*;
import com.example.springsecurityauthtwo.security.model.entities.*;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.services.UserDetailsImpl;
import com.example.springsecurityauthtwo.security.services.UserServices;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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

    private AuthenticationManager manager;
    private UserServices userServices;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private JwtUtils jwtUtils;


    @Override
    @PostMapping("/signing")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // context and auth management
        log.info("User signing in...");
        Authentication auth = manager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetailsImpl) auth.getPrincipal();

        // jwt generation
        Map<String, String> tokens = jwtUtils.generateTokenAndRefresh(userDetails.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_TOKEN, SecurityConstants.TOKEN_START + tokens.get("token"));
        headers.add(SecurityConstants.REFRESH_TOKEN, SecurityConstants.TOKEN_START_REFRESH + tokens.get("refreshToken"));
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        // build and send response
        UserInfoResponse infoResponse = new UserInfoResponse(userDetails.getUsername(), roles);
        log.info("user signed in");

        return new ResponseEntity<>(infoResponse, headers, HttpStatus.OK);
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

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
        Set<String> strRoles = signupRequest.getRoles();
        Set<AppRole> roles = new HashSet<>();

        if (strRoles == null) {
            AppRole userRoles = userServices.findRoleByRolename(ERole.ROLE_USER);
            roles.add(userRoles);
        } else {
            userServices.manageRoles(strRoles, roles);
        }

        user.setRoles(roles);
        userServices.saveNewUser(user);
        log.info("user registered successfully!");
        responseBody.put(SecurityConstants.SUCCESS, String.format("User %s registered successfully", user.getUsername()));

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseBody);
    }

    @Override
    @GetMapping("/refreshToken")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {

        Map<String, Object> responseBody = new HashMap<>();

        String refreshToken = jwtUtils.getRefreshTokenFromHeaders(request);

        if (StringUtils.isEmpty(refreshToken)) {
            responseBody.put(SecurityConstants.ERROR, "no refresh token provided");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }

        try {

            String username = jwtUtils.getUsernameFromToken(refreshToken);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (Boolean.FALSE.equals(jwtUtils.validateToken(refreshToken, user))) {
                responseBody.put(SecurityConstants.ERROR, SecurityConstants.INVALID_REFRESH);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(responseBody);
            }

            String newAccessToken = jwtUtils.generateJwtToken(username);
            responseBody.put("newToken", SecurityConstants.TOKEN_START + newAccessToken);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseBody);

        } catch (Exception e) {
            responseBody.put(SecurityConstants.ERROR, SecurityConstants.INVALID_REFRESH);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(responseBody);
        }
    }

    @Override
    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody SignupRequest userDto, HttpServletRequest request) {

        Map<String, Object> responseBody = new HashMap<>();
        String username = jwtUtils.getUsernameFromToken(jwtUtils.getTokenFromHeaders(request));
        AppUser updatedUser = userServices.updateUserInfo(userDto, username);

        if (Objects.nonNull(updatedUser)) {
            List<String> roles = updatedUser.getRoles().stream()
                    .map(role -> String.valueOf(role.getName()))
                    .collect(Collectors.toList());

            UserInfoResponse responseInfo = new UserInfoResponse().setUsername(updatedUser.getUsername())
                    .setRoles(roles);
            log.info("user updated successfully");

            if (!Objects.equals(username, userDto.getUsername())) {
                String newToken = jwtUtils.generateJwtToken(userDto.getUsername());
                responseBody.put("token", newToken);
            }
            responseBody.put("data", responseInfo);
            return ResponseEntity.ok(responseBody);
        } else {
            log.info("fail to update user");
            responseBody.put(SecurityConstants.ERROR, "Could not update user.");
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @Override
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateSelectedUser(@PathVariable String userId, @RequestBody SignupRequest updatedUser) {
        // TODO meilleure response qu'une simple string ?
        MessageResponse response = new MessageResponse();
        AppUser user = userServices.findById(Long.parseLong(userId));

        if (Objects.nonNull(user)) {
            String username = user.getUsername();
            AppUser updatedUserReturn = userServices.updateUserInfo(updatedUser, username);

            if (Objects.nonNull(updatedUserReturn)) {
                response.setMessage(String.format("user %s updated successfully", username));
                return ResponseEntity.status(HttpStatus.OK).body(response);

            } else {
                response.setMessage("something went wrong when updating the user: please contact an administrator.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } else {
            response.setMessage("could not update user : user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> deleteUser(HttpServletRequest request, DeleteRequestConfirmation deleteRequest) {
        MessageResponse response = new MessageResponse();
        if (deleteRequest.isDeleteRequest() && deleteRequest.isConfirmedDeleteRequest()) {
            String username = jwtUtils.getUsernameFromToken(jwtUtils.getTokenFromHeaders(request));
            AppUser user = this.userServices.findUserByUsername(username);
            userServices.deleteUser(user);
            response.setMessage("your account have been correctly deleted");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setMessage("your account hasn't been deleted: missing confirmation");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @Override
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteSelectedUser(@PathVariable String userId) {

        MessageResponse response = new MessageResponse();
        AppUser user = userServices.findById(Long.getLong(userId));

        if (Objects.nonNull(user)) {
            userServices.deleteUser(user);
            response.setMessage(String.format("User %s delete successfully", user.getUsername()));
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("can't delete the user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public void testController() {
    }


}