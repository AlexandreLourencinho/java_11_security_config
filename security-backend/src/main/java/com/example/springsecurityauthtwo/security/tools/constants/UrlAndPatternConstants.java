package com.example.springsecurityauthtwo.security.tools.constants;

public final class UrlAndPatternConstants {

    private UrlAndPatternConstants() {
    }

    /* ---------------------------------------- url strings ---------------------------------------- */
    public static final String PUBLIC_URL = "/public/";

    /* url string dev specific */
    public static final String H2_URL = "/h2-console";
    public static final String SWAGGER_URL = "/swagger-ui";
    public static final String SWAGGER_RESOURCES_URL = "/swagger-ui";
    public static final String V2_API_DOCS_URL = "/v2/api-docs";
    public static final String V3_API_DOCS_URL = "/v3/api-docs";
    public static final String ACTUATOR_URL = "/actuator";

    /* pattern url */
    public static final String PATTERN_PUBLIC_URL = "/**/public/**";

    /* dev-specific pattern URL */
    public static final String SWAGGER_UI_PATTERN_URL = "/swagger-ui/**";
    public static final String WEBJARS_URL = "/webjars/**";
    public static final String H2_CONSOLE_PATTERN_URL = "/h2-console/**";
    public static final String V2_API_DOCS_URL_PATTERN = "/v2/api-docs/**";
    public static final String V3_API_DOCS_URL_PATTERN = "/v3/api-docs/**";
    public static final String SWAGGER_RESOURCES_PATTERN_URL = "/swagger-resources/**";
    public static final String ACTUATOR_PATTERN_URL = "/actuator/**";


    /* dev patterns and url */
    private static final String[] AUTHORIZED_URL_PATTERN_DEV = {PATTERN_PUBLIC_URL, SWAGGER_UI_PATTERN_URL, WEBJARS_URL, H2_CONSOLE_PATTERN_URL,
            V2_API_DOCS_URL_PATTERN, V3_API_DOCS_URL_PATTERN, SWAGGER_RESOURCES_PATTERN_URL, ACTUATOR_PATTERN_URL};
    private static final String[] AUTHORIZED_URL_DEV = {PUBLIC_URL, H2_URL, SWAGGER_URL, SWAGGER_RESOURCES_URL, V2_API_DOCS_URL, V3_API_DOCS_URL, ACTUATOR_URL};

    /* prod pattern and url */
    private static final String[] AUTHORIZED_URL_PATTERN = {PATTERN_PUBLIC_URL};
    private static final String[] AUTHORIZED_URL = {PUBLIC_URL};

    public static String[] getAuthorizedPatternDev() {
        return AUTHORIZED_URL_PATTERN_DEV;
    }

    public static String[] getAuthorizedPattern() {
        return AUTHORIZED_URL_PATTERN;
    }

    public static String[] getAuthorizedUrlDev() {
        return AUTHORIZED_URL_DEV;
    }

    public static String[] getAuthorizedUrl() {
        return AUTHORIZED_URL;
    }


}
