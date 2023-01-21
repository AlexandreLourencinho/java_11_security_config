package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.jwt.JwtUtils;
import com.example.springsecurityauthtwo.security.model.dtos.*;
import com.example.springsecurityauthtwo.security.model.entities.*;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.services.TokenService;
import com.example.springsecurityauthtwo.security.services.UserDetailsImpl;
import com.example.springsecurityauthtwo.security.services.UserServices;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the authentification, account creation, role management, etc
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class AuthController {

    private AuthenticationManager manager;
    private TokenService tokenService;
    private UserServices userServices;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    /**
     * Read - Path allowing user to connect
     * @param loginRequest the login request that contains username and password
     * @return users infos with access and refresh token
     */
    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // context and auth management
        log.info("User signin in...");
        Authentication auth = manager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetailsImpl) auth.getPrincipal();

        // jwt generation
        Map<String, String> bothToken = jwtUtils.generateTokenAndRefresh(userDetails.getUsername());
        String jwt = bothToken.get("token");
        String refreshToken = bothToken.get("refreshToken");
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        // headers management
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_TOKEN, SecurityConstants.TOKEN_START + jwt);
        headers.add(SecurityConstants.REFRESH_TOKEN, SecurityConstants.TOKEN_START_REFRESH + refreshToken);

        // reset in database if  present, insert otherwise
        Boolean doesHeHasJwt = tokenService.isUserHadJwt(userDetails.getUsername());
        if(Boolean.TRUE.equals(doesHeHasJwt)) {
            tokenService.razUserJwt(jwt, refreshToken, userDetails.getUsername());
        } else {
            tokenService.registerJwtUser(jwt, refreshToken, userDetails.getUsername());
        }

        // build and send response
        UserInfoResponse infoResponse = new UserInfoResponse(userDetails.getUsername(), roles);
        log.info("user signed in");
        return new ResponseEntity<>(infoResponse, headers, HttpStatus.OK);

    }

    /**
     * Write - Path allowing a user to create an account
     * @param signupRequest Object that contains username mail password and roles. default role is user
     * @return A string that signify if the user is created or not
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        log.warn("registering a new user...");

        if (Boolean.TRUE.equals(userServices.usernameAlreadyExists(signupRequest.getUsername()))) {
            log.warn("The username is already taken");
            return ResponseEntity.badRequest().body(new MessageResponse(SecurityConstants.ERROR_USERNAME_TAKEN));
        }

        if (Boolean.TRUE.equals(userServices.emailAlreadyExists(signupRequest.getEmail()))) {
            log.warn("The email is already taken");
            return ResponseEntity.badRequest().body(new MessageResponse(SecurityConstants.ERROR_MAIL_TAKEN));
        }

        AppUser user = new AppUser()
                .setUsername(signupRequest.getUsername())
                .setEmail(signupRequest.getEmail())
                .setPassword(passwordEncoder.encode(signupRequest.getPassword()));
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
        return ResponseEntity.ok(new MessageResponse(String.format("User %s registered successfully", user.getUsername())));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public void testController() {
        // TODO document method here just to test the token
    }

}