package com.example.springsecurityauthtwo.security.jwt.implementations;

import com.example.springsecurityauthtwo.security.jwt.interfaces.JwtUtils;
import static com.example.springsecurityauthtwo.security.tools.constants.TokenConstants.*;

import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;

import java.util.*;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.function.Function;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;


/**
 * the tools class for the jwt management
 *
 * @author Lourencinho Alexandre
 * @version 1.1.0
 */
@Slf4j
@Component
public class JwtUtilsImpl implements JwtUtils {

    private final String secret;
    private final String minuteExpiration;

    @Autowired
    public JwtUtilsImpl( @Value("${com.example.jwtSecret}") String secret,
                         @Value("${com.example.minuteExpiration}") String minuteExpiration) {
        this.secret = secret;
        this.minuteExpiration = minuteExpiration;
    }

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
                .claims(claims)
                .subject(username)
                .issuedAt(date)
                .expiration(expDate)
                .signWith(getSecretKey())
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
                .claims(claims)
                .subject(username)
                .issuedAt(date)
                .expiration(refreshDate)
                .signWith(getSecretKey()).compact();
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
    public Boolean validateToken(String token, UserDetailsCustom user) {

        log.info("checking if token is still valid...");
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String getTokenFromHeaders(HttpServletRequest request) {
        return request.getHeader(HEADER_TOKEN).substring(BEARER_SUBSTRING);
    }

    @Override
    public String getRefreshTokenFromHeaders(HttpServletRequest request) {
        return request.getHeader(REFRESH_TOKEN).substring(REFRESH_SUBSTRING);
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
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * gather the built-in secretKey from the private secret key string
     *
     * @return a {@link SecretKey} object built from the private secret key
     */
    private SecretKey getSecretKey() {
        log.info("secret : {}", secret);
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

}
