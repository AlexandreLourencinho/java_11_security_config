package com.example.springsecurityauthtwo.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    @Value("${com.example.jwtSecret}")
    private String secret;
    @Value("${com.example.jwtExpirationMs}")
    private String expiration;
    @Value("${com.example.jwtCookieName}")
    private String cookie;

    public String generateTokenFromUsername(String username) {
        Date date = new Date();
        Date expDate = new Date(date.getTime() + Integer.parseInt(expiration));
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    public String generateToken(UserDetails user) {
        Date date = new Date();
        Date expDate = new Date(date.getTime() + Integer.parseInt(expiration));
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(date)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
//        Base64.getEncoder().encode(secret.getBytes())
        return Jwts.parser().setSigningKey(Base64.getEncoder().encode(secret.getBytes())).parseClaimsJwt(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails user) {
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }


}
