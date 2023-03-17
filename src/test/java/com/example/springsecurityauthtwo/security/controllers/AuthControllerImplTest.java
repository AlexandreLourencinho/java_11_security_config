package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.model.dtos.LoginRequest;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.services.UserServices;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(SecurityConstants.SIGNUP_URL)
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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(SecurityConstants.SIGNING_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String header = result.getResponse().getHeader(SecurityConstants.HEADER_TOKEN);
        assert header != null;
        assertTrue(header.startsWith(SecurityConstants.TOKEN_START));
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals(this.username, responseBody.get("username"));
        assertEquals("[ROLE_USER]", responseBody.get("roles").toString());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin" , roles = {"ADMIN"})
    void testUpdateSelectedUser() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ACTUATOR");
        SignupRequest signupRequest = new SignupRequest()
                .setEmail(this.email + "1")
                .setUsername(this.username + "1")
                .setPassword(this.password + "1")
                .setRoles(roles);
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});
        System.out.println("respBody : " + responseBody);
        assertEquals("user test_user updated successfully", responseBody.get("message"));
    }
}