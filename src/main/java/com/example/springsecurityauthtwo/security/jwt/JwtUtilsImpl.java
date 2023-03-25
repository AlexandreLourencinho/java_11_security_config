package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.tools.SecurityConstants;

import java.util.*;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

/**
 * the tools class for the jwt management
 *
 * @author Lourencinho Alexandre
 * @version 1.1.0
 */
@Slf4j
@Component
public class JwtUtilsImpl implements JwtUtils {

    @Value("${com.example.jwtSecret}")
    private String secret;
    @Value("${com.example.minuteExpiration}")
    private String minuteExpiration;


    @Override
    public Map<String, String> generateTokenAndRefresh(String username) {
        log.info("generating new Access Token and new Refresh Token...");
        String refreshToken = generateJwtRefreshToken(username);
        String token = generateJwtToken(username);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", token);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Override
    public String generateJwtToken(String username) {
        log.info("generating access token...");
        Map<String, Object> claims = new HashMap<>();
        Date date = new Date(System.currentTimeMillis());
        Date expDate = Date.from(
                LocalDateTime.now()
                        .plusMinutes(Long.parseLong(minuteExpiration))
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public String generateJwtRefreshToken(String username) {
        log.info("generating refresh token...");
        Date date = new Date(System.currentTimeMillis());
        Map<String, Object> claims = new HashMap<>();
        Date refreshDate = Date.from(
                LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(refreshDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public Boolean validateToken(String token, UserDetails user) {
        log.info("checking if token is still valid...");
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String getTokenFromHeaders(HttpServletRequest request) {
        return request.getHeader(SecurityConstants.HEADER_TOKEN).substring(SecurityConstants.BEARER_SUBSTRING);
    }

    @Override
    public String getRefreshTokenFromHeaders(HttpServletRequest request) {
        return request.getHeader(SecurityConstants.REFRESH_TOKEN).substring(SecurityConstants.REFRESH_SUBSTRING);
    }

    /**
     * check if token is expired
     *
     * @param token the jwt token
     * @return true or false (or null)
     */
    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    /**
     * Get all claims from a JWT Token
     *
     * @param token the jwt token, access or refresh
     * @return Claims object
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}
