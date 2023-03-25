package com.example.springsecurityauthtwo.security.jwt.interfaces;

import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * the tools class for the jwt management
 *
 * @author Lourencinho Alexandre
 * @version 1.1.0
 */
public interface JwtUtils {

    /**
     * Method generating access and refresh token
     * @param username the user's username
     * @return a Map containing both tokens
     */
    Map<String, String> generateTokenAndRefresh(String username);

    /**
     * Creation method for the access token
     * @param username the user's username
     * @return a string jwt
     */
    String generateJwtToken(String username);

    /**
     * Creation method for the refresh token
     * @param username the user's username
     * @return a string jwt
     */
    String generateJwtRefreshToken(String username);

    /**
     * Get a specific claim from a Claims object
     * @param token the JWT token
     * @param claimsResolver a function
     * @param <T> claim type, expiration date, subject..
     * @return a claim
     */
    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    /**
     * get username from the JWT token claims
     * @param token jwt token
     * @return a string username
     */
    String getUsernameFromToken(String token);

    /**
     * get token expiration date
     * @param token the jwt token
     * @return a date type
     */
    Date getExpirationDateFromToken(String token);

    /**
     * check if the jwt token is valid
     * @param token the jwt token
     * @param user the user associated with
     * @return true false or null
     */
    Boolean validateToken(String token, UserDetailsCustom user);

    /**
     * Get the given jwt token from the headers
     * @param request the httpServlet request object where the headers are
     * @return the token
     */
    String getTokenFromHeaders(HttpServletRequest request);

    /**
     * Get the given refresh token from the headers
     * @param request the httpServlet request object where the headers are
     * @return the token
     */
    String getRefreshTokenFromHeaders(HttpServletRequest request);
}
