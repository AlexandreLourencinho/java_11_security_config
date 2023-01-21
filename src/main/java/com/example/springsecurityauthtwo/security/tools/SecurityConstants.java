package com.example.springsecurityauthtwo.security.tools;

/**
 * Constants used in security package
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String ERROR_ROLE = "Error: role not found";
    public static final String HEADER_TOKEN = "Authorization";
    public static final String TOKEN_START = "Bearer ";
    public static final String TOKEN_START_REFRESH = "Refresh ";
    public static final String REFRESH_TOKEN = "Refresh";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String PATH = "path";
    public static final String EXPIRED = "expired";
    public static final String REFRESHED_TOO_MUCH = "Refresh token used too many time. Please reconnect.";
    public static final String ERROR_USERNAME_TAKEN = "Error : Username is already taken !";
    public static final String ERROR_MAIL_TAKEN = "Error : Email is already use !";



    public static final int MAX_REFRESH = 4;
    public static final int BEARER_SUBSTRING = 7;
    public static final int REFRESH_SUBSTRING = 8;
}
