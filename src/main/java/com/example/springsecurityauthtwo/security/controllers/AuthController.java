package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.jwt.JwtUtils;
import com.example.springsecurityauthtwo.security.model.dtos.LoginRequest;
import com.example.springsecurityauthtwo.security.model.dtos.MessageResponse;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.model.dtos.UserInfoResponse;
import com.example.springsecurityauthtwo.security.model.entities.*;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.repositories.AppRoleRepository;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.services.UserDetailsImpl;
import com.example.springsecurityauthtwo.security.tools.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class AuthController {

    private AuthenticationManager manager;
    private AppUserRepository userRepository;
    private AppRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication auth = manager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        String jwt = jwtUtils.generateToken(userDetails);
        log.info(jwt);
//        System.out.println(jwtCookie);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.HEADER_TOKEN, Constants.TOKEN_START + jwt);
        UserInfoResponse infoResponse = new UserInfoResponse(userDetails.getUsername(), roles);
        return new ResponseEntity<>(infoResponse, headers, HttpStatus.OK);
//                .body(new UserInfoResponse(userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        roles));


    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if(Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Username is already taken !"));
        }

        if(Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Email is already use !"));
        }

        AppUser user = new AppUser()
                .setUsername(signupRequest.getUsername())
                .setEmail(signupRequest.getEmail())
                .setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<AppRole> roles = new HashSet<>();
        if (strRoles == null) {
            AppRole userRoles = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(Constants.ERROR_ROLE));
            roles.add(userRoles);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        AppRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException(Constants.ERROR_ROLE));
                        roles.add(adminRole);
                        break;
                    case "mod" :
                        AppRole modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException(Constants.ERROR_ROLE));
                        roles.add(modRole);
                        break;
                default :
                        AppRole userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException(Constants.ERROR_ROLE));
                        roles.add(userRole);
                 break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(String.format("User %s registered successfully", user.getUsername())));
    }


    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been signed out !"));
    }




}
