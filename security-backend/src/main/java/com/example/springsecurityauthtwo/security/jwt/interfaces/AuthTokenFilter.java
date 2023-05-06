package com.example.springsecurityauthtwo.security.jwt.interfaces;

import javax.servlet.http.HttpServletRequest;

public interface AuthTokenFilter {

    /**
     * Manage the JWT authentication
     *
     * @param request the http servlet request object
     * @param jwt     the jwt
     */
    void manageJwtAuthentication(HttpServletRequest request, String jwt);

    /**
     * method that manage all the Exception call when an invalid JWT is passed
     *
     * @param request the http servlet request object
     * @param jwt     the jwt
     */
    void manageJwtAuthAndErrors(HttpServletRequest request, String jwt);
}
