package com.example.springsecurityauthtwo.security.tools.constants;

public final class TokenConstants {

    private TokenConstants() {
    }

    /* tokens strings */
    public static final String HEADER_TOKEN = "Authorization";
    public static final String TOKEN_START = "Bearer ";
    public static final String TOKEN_START_REFRESH = "Refresh ";
    public static final String REFRESH_TOKEN = "Refresh";
    public static final String STATUS = "Status";
    public static final String SUCCESS = "Success";
    public static final String MESSAGE = "Message";
    public static final String PATH = "Path";

    /* ---------------------------------------- number constants ---------------------------------------- */
    public static final int BEARER_SUBSTRING = 7;
    public static final int REFRESH_SUBSTRING = 8;
}
