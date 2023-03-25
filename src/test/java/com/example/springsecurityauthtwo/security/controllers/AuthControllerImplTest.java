package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.services.UserServices;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.example.springsecurityauthtwo.security.model.dtos.LoginRequest;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;

import java.util.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServices userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "test_user";
    private final String email = "test_user@test.com";
    private final String password = "test_password";

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testRegisterUser() throws Exception {
        SignupRequest request = new SignupRequest()
                .setUsername(this.username)
                .setEmail(this.email)
                .setPassword(this.password)
                .setRoles(Collections.singleton("ROLE_USER"));

        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals("User test_user registered successfully", responseBody.get("Success"));

        AppUser user = userServices.findUserByUsername(this.username);
        assertNotNull(user);
        assertEquals(this.username, user.getUsername());
        assertEquals(this.email, user.getEmail());
        assertTrue(passwordEncoder.matches(this.password, user.getPassword()));
        Hibernate.initialize(user.getRoles());
        AppRole role = user.getRoles().iterator().next();

        assertEquals(ERole.ROLE_USER.toString(), role.getName().toString());
    }

    @Test
    @Order(2)
    void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest()
                .setUsername(this.username)
                .setPassword(this.password);
        String requestBody = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/public/signing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String header = result.getResponse().getHeader(SecurityConstants.HEADER_TOKEN);
        assert header != null;
        assertTrue(header.startsWith(SecurityConstants.TOKEN_START));
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});
        @SuppressWarnings("unchecked") LinkedHashMap<String, Object> responseUser = (LinkedHashMap<String, Object>) responseBody.get("user");
        assertEquals(this.username, responseUser.get("username"));
        assertEquals("[ROLE_USER]", responseUser.get("roles").toString());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateSelectedUser() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ACTUATOR");
        SignupRequest signupRequest = new SignupRequest()
                .setEmail(this.email + "1")
                .setUsername(this.username + "1")
                .setPassword(this.password + "1")
                .setRoles(roles);
        AppUser updatedUser = userServices.findUserByUsername(this.username);
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/update/" + updatedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals("user test_user updated successfully", responseBody.get("Success"));
        List<AppUser> listUser = userServices.findAll();
        assertTrue(listUser.stream().anyMatch(user -> Objects.equals(user.getUsername(), signupRequest.getUsername())));
    }
}