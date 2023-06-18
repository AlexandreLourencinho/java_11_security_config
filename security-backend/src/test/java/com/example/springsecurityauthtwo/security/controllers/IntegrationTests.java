package com.example.springsecurityauthtwo.security.controllers;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.dtos.LoginRequest;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserServices;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
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

import static com.example.springsecurityauthtwo.security.tools.constants.TokenConstants.HEADER_TOKEN;
import static com.example.springsecurityauthtwo.security.tools.constants.TokenConstants.TOKEN_START;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTests {

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
    private String jwt = null;

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
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertEquals("User test_user registered successfully", responseBody.get("Success"));

        AppUser user = userServices.findUserByUsername(this.username);
        assertNotNull(user);
        assertEquals(this.username, user.getUsername());
        assertEquals(this.email, user.getEmail());
        assertTrue(passwordEncoder.matches(this.password, user.getPassword()));
        Hibernate.initialize(user.getRoles());
        AppRole role = user.getRoles().iterator().next();

        assertEquals(ERole.ROLE_USER.getValue(), role.getName());
    }

    @Test
    @Order(2)
    void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest()
                .setUsername(this.username)
                .setPassword(this.password);
        MvcResult result = loginRequest(loginRequest);
        String header = result.getResponse().getHeader(HEADER_TOKEN);
        assert header != null;
        assertTrue(header.startsWith(TOKEN_START));
        jwt = header;
        log.info("header : {}", header);
        log.info("jwt : {}", jwt);
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {
        });
        @SuppressWarnings("unchecked") LinkedHashMap<String, Object> responseUser = (LinkedHashMap<String, Object>) responseBody.get("user");
        assertEquals(this.username, responseUser.get("username"));
        assertEquals("[User]", responseUser.get("roles").toString());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", authorities = {"Admin"})
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
        Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {
        });
        assertEquals("user test_user updated successfully", responseBody.get("Success"));
        List<AppUser> listUser = userServices.findAll();
        assertTrue(listUser.stream().anyMatch(user -> Objects.equals(user.getUsername(), signupRequest.getUsername())));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", authorities = {"Admin"})
    void testGetListUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("admin")))
                .andExpect(jsonPath("$[0].email", is("admin@admin")))
                .andExpect(jsonPath("$[1].username", is("test_user1")))
                .andExpect(jsonPath("$[1].email", is("test_user@test.com1")))
                .andReturn();
        log.info("result : {} ", result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user", authorities = {"User"})
    void testGetOwnUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest()
                .setUsername(this.username + "1")
                .setPassword(this.password + "1");
        MvcResult resultLogin = loginRequest(loginRequest);
        String header = resultLogin.getResponse().getHeader(HEADER_TOKEN);
        log.info("header : {} ", header);
        jwt = header;
        log.info("jwt : {}", jwt);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user").header("Authorization", this.jwt))
                .andExpect(status().isOk()).andReturn();
        log.info("results : {}", result.getResponse().getContentAsString());
    }

    private MvcResult loginRequest(LoginRequest loginRequest) throws Exception {
        String requestBody = objectMapper.writeValueAsString(loginRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post("/user/public/signing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
    }
}