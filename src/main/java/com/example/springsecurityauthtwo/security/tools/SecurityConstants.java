package com.example.springsecurityauthtwo.security.tools;

/**
 * Constants used in security package
 *
 * @author Alexandre Lourencinho
 * @version 1.2
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    /* ------------------------------------------------------------------------------------------------------ */
    /* ------------------------------------------ String constants ------------------------------------------ */
    /* ------------------------------------------------------------------------------------------------------ */


    /* ---------------------------------------- profiles ---------------------------------------- */
    public static final String DEV_ENV = "dev";
    public static final String TEST_ENV = "test";


    /* ---------------------------------------- format string ---------------------------------------- */
    public static final String ERROR_MANAGEMENT = "{} : {}";


    /* ---------------------------------------- url strings ---------------------------------------- */
    public static final String PUBLIC_URL = "/public/";

    /* url string dev specific */
    public static final String H2_URL = "/h2-console";
    public static final String SWAGGER_URL = "/swagger-ui";
    public static final String SWAGGER_RESOURCES_URL = "/swagger-ui";
    public static final String V2_API_DOCS_URL = "/v2/api-docs";
    public static final String V3_API_DOCS_URL = "/v3/api-docs";

    /* pattern url */
    public static final String PATTERN_PUBLIC_URL = "/**/public/**";

    /* dev-specific pattern URL */
    public static final String SWAGGER_UI_PATTERN_URL = "/swagger-ui/**";
    public static final String WEBJARS_URL = "/webjars/**";
    public static final String H2_CONSOLE_PATTERN_URL = "/h2-console/**";
    public static final String V2_API_DOCS_URL_PATTERN = "/v2/api-docs/**";
    public static final String V3_API_DOCS_URL_PATTERN = "/v3/api-docs/**";
    public static final String SWAGGER_RESOURCES_PATTERN_URL = "/swagger-resources/**";

    /* ---------------------------------------- errors keys strings ---------------------------------------- */
    public static final String ERROR = "Error";
    public static final String UNAUTHORIZED = "Unauthorized";

    /* error keys */
    public static final String EXPIRED = "Expired";
    public static final String CLAIMS_INVALID = "Invalid";
    public static final String MALFORMED = "Malformed";
    public static final String INCORRECT_TOKEN_FORMAT = "FormatError";
    public static final String SIGNATURE = "SignatureError";
    public static final String UNSUPPORTED = "UnsupportedError";
    public static final String NO_BEARER = "NoBearerError";
    public static final String INTERNAL_SERVER_ERROR = "InternalError";

    /* Entities error*/
    public static final String ERROR_ROLE = "Error: role not found. ";
    public static final String ERROR_USERNAME_TAKEN = "Error : Username is already taken. ";
    public static final String ERROR_MAIL_TAKEN = "Error : Email is already use. ";
    public static final String USER_NOT_FOUND = "Error: user not found";

    /* errors message strings */
    public static final String INVALID_TOKEN_MESSAGE = "Token is invalid. ";
    public static final String INVALID_REFRESH = "Refresh token is invalid. ";
    public static final String EXPIRED_ERROR_MESSAGE = EXPIRED + " - JWT Token has expired. ";
    public static final String CLAIMS_INVALID_MESSAGE = CLAIMS_INVALID + " - Data stored in jwt token are invalid. ";
    public static final String INCORRECT_TOKEN_FORMAT_MESSAGE = INCORRECT_TOKEN_FORMAT + " - The token can't be parsed. ";
    public static final String MALFORMED_JWT_MESSAGE = MALFORMED + " - JWT was not correctly constructed. ";
    public static final String SIGNATURE_MESSAGE = SIGNATURE + " - Can't verify the JWT signature. ";
    public static final String UNSUPPORTED_MESSAGE = UNSUPPORTED + " - The jwt is in an unsupported format. ";
    public static final String NO_BEARER_MESSAGE = NO_BEARER + " - string doesn't start with Bearer. ";
    public static final String UNAUTHORIZED_MESSAGE = UNAUTHORIZED + " - We don't know what happened but something went wrong";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = INTERNAL_SERVER_ERROR + " - an internal error occurred in the server : please contact an administrator";

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


    /* ---------------------------------------- constants arrays ---------------------------------------- */
    /* errors list */
    private static final String[] ERRORS_LIST = {EXPIRED, CLAIMS_INVALID, MALFORMED, INCORRECT_TOKEN_FORMAT, SIGNATURE,
            UNSUPPORTED, NO_BEARER, INTERNAL_SERVER_ERROR};

    /* error messages list */
    private static final String[] ERRORS_MESSAGE_LIST = {EXPIRED_ERROR_MESSAGE, CLAIMS_INVALID_MESSAGE,
            INVALID_TOKEN_MESSAGE, MALFORMED_JWT_MESSAGE, INCORRECT_TOKEN_FORMAT_MESSAGE, SIGNATURE_MESSAGE,
            UNSUPPORTED_MESSAGE, NO_BEARER_MESSAGE, INTERNAL_SERVER_ERROR_MESSAGE};


    /* ----------------------------------------  URL and pattern array authorization  ---------------------------------------- */

    /* dev patterns and url */
    private static final String[] AUTHORIZED_URL_PATTERN_DEV = {PATTERN_PUBLIC_URL, SWAGGER_UI_PATTERN_URL, WEBJARS_URL, H2_CONSOLE_PATTERN_URL, V2_API_DOCS_URL_PATTERN, V3_API_DOCS_URL_PATTERN, SWAGGER_RESOURCES_PATTERN_URL};
    private static final String[] AUTHORIZED_URL_DEV = {PUBLIC_URL, H2_URL, SWAGGER_URL, SWAGGER_RESOURCES_URL, V2_API_DOCS_URL, V3_API_DOCS_URL};

    /* prod pattern and url */
    private static final String[] AUTHORIZED_URL_PATTERN = {PATTERN_PUBLIC_URL};
    private static final String[] AUTHORIZED_URL = {PUBLIC_URL};

    /**
     * return the list of errors managed
     *
     * @return an array containing string error keys
     */
    public static String[] getErrorList() {
        return ERRORS_LIST;
    }

    /**
     * return the list of error messages managed
     *
     * @return an array of string error messages
     */
    public static String[] getErrorsMessageList() {
        return ERRORS_MESSAGE_LIST;
    }

    /**
     * get the authorized pattern for dev/test env or prod env
     *
     * @param isDevEnv a boolean, true if env is dev or test
     * @return an array of string containing the authorized pattern
     */
    public static String[] getAuthorizedUrlPattern(Boolean isDevEnv) {
        if (Boolean.TRUE.equals(isDevEnv)) {
            return AUTHORIZED_URL_PATTERN_DEV;
        } else {
            return AUTHORIZED_URL_PATTERN;
        }
    }

    /**
     * get the authorized url depending on the profile
     *
     * @param isDevEnv a boolean, true if env is dev or test
     * @return an array of string, containing the authorized url
     */
    public static String[] getAuthorizedUrl(Boolean isDevEnv) {
        if (Boolean.TRUE.equals(isDevEnv)) {
            return AUTHORIZED_URL_DEV;
        } else {
            return AUTHORIZED_URL;
        }
    }

    /**
     * check the active profile
     *
     * @param profile the current active profile
     * @return a boolean, true is profile is dev or env, false otherwise
     */
    public static Boolean isDevOrTestEnv(String profile) {
        return (profile.equals(DEV_ENV) || profile.equals(TEST_ENV));
    }

}
