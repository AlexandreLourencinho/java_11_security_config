package com.example.springsecurityauthtwo.security.jwt;

import java.util.Date;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.userdetails.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JwtUtilsImplTest {

    @Autowired
    private JwtUtilsImpl jwtUtils;

    private static final String USERNAME = "testUser";

    @Test
    void testGenerateJwtToken() {
        String token = jwtUtils.generateJwtToken(USERNAME);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtUtils.generateJwtToken(USERNAME);
        String username = jwtUtils.getUsernameFromToken(token);
        assertEquals(USERNAME, username);
    }

    @Test
    void testGetExpirationDateFromToken() {
        String token = jwtUtils.generateJwtToken(USERNAME);
        Date expirationDate = jwtUtils.getExpirationDateFromToken(token);
        assertNotNull(expirationDate);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtils.generateJwtToken(USERNAME);
        UserDetails userDetails = new User(USERNAME, "", new ArrayList<>()); // TODO: Trouver une solution pour ça et le user custom ?
        Boolean valid = jwtUtils.validateToken(token, userDetails);
        assertTrue(valid);
    }

}