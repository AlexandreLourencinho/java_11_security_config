package com.example.springsecurityauthtwo.security.tools;

/**
 * Constants used in security package
 *
 * @author Alexandre Lourencinho
 * @version 1.1
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    /* * --- String constants --- * */

    /* * --- profiles --- * */
    public static final String DEV_ENV = "dev";

    /* format string */
    public static final String ERROR_MANAGEMENT = "{} : {}";

    /* url strings */
    public static final String PUBLIC_URL = "/public/";

    /* url matching for permitAll() */
    public static final String PATTERN_PUBLIC_URL = "/user/public/**";

    /* dev-specific URL */
    public static final String V2_API_DOCS_URL = "/v2/api-docs";
    public static final String SWAGGER_RESOURCES_URL = "/swagger-resources";
    public static final String SWAGGER_RESOURCES_PATTERN_URL = "/swagger-resources/**";
    public static final String CONFIGURATION_UI_URL = "/configuration/ui";
    public static final String CONFIGURATION_SECURITY_URL = "/configuration/security";
    public static final String SWAGGER_UI_HTML_URL = "/swagger-ui.html";
    public static final String WEBJARS_URL = "/webjars/**";
    public static final String V3_API_DOCS_URL = "/v3/api-docs/**";
    public static final String SWAGGER_UI_PATTERN_URL = "/swagger-ui/**";
    public static final String H2_CONSOLE_URL = "/h2-console";
    public static final String H2_CONSOLE_PATTERN_URL = "/h2-console/**";

    /* errors keys strings */
    public static final String ERROR = "Error";
    public static final String UNAUTHORIZED = "Unauthorized";

    public static final String EXPIRED = "Expired";
    public static final String CLAIMS_INVALID = "Invalid";
    public static final String MALFORMED = "Malformed";
    public static final String INCORRECT_TOKEN_FORMAT = "FormatError";
    public static final String SIGNATURE = "SignatureError";
    public static final String UNSUPPORTED = "UnsupportedError";
    public static final String NO_BEARER = "NoBearerError";

    /* Entities error*/
    public static final String ERROR_ROLE = "Error: role not found. ";
    public static final String ERROR_USERNAME_TAKEN = "Error : Username is already taken. ";
    public static final String ERROR_MAIL_TAKEN = "Error : Email is already use. ";
    public static final String USER_NOT_FOUND = "Error: user not found";

    /* not valid token errors */
    public static final String INVALID_TOKEN_MESSAGE = "Token is invalid. ";
    public static final String INVALID_REFRESH = "Refresh token is invalid. ";

    /* errors message strings */
    public static final String EXPIRED_ERROR_MESSAGE = EXPIRED + " - JWT Token has expired. ";
    public static final String CLAIMS_INVALID_MESSAGE = CLAIMS_INVALID + " - Data stored in jwt token are invalid. ";
    public static final String INCORRECT_TOKEN_FORMAT_MESSAGE = INCORRECT_TOKEN_FORMAT + " - The token can't be parsed. ";
    public static final String MALFORMED_JWT_MESSAGE = MALFORMED + " - JWT was not correctly constructed. ";
    public static final String SIGNATURE_MESSAGE = SIGNATURE + " - Can't verify the JWT signature. ";
    public static final String UNSUPPORTED_MESSAGE = UNSUPPORTED + " - The jwt is in an unsupported format. ";
    public static final String NO_BEARER_MESSAGE = NO_BEARER + " - string doesn't start with Bearer. ";
    public static final String UNAUTHORIZED_MESSAGE = UNAUTHORIZED + " - We don't know what happened but something went wrong";


    /* tokens strings */
    public static final String HEADER_TOKEN = "Authorization";
    public static final String TOKEN_START = "Bearer ";
    public static final String TOKEN_START_REFRESH = "Refresh ";
    public static final String REFRESH_TOKEN = "Refresh";
    public static final String STATUS = "Status";
    public static final String SUCCESS = "Success";
    public static final String MESSAGE = "Message";
    public static final String PATH = "Path";


    /* * --- number constants --- * */
    public static final int BEARER_SUBSTRING = 7;
    public static final int REFRESH_SUBSTRING = 8;


    /* * --- constants arrays --- * */
    // errors list
    private static final String[] ERRORS_LIST = {EXPIRED, CLAIMS_INVALID, MALFORMED, INCORRECT_TOKEN_FORMAT, SIGNATURE,
            UNSUPPORTED, NO_BEARER};

    // error messages list
    private static final String[] ERRORS_MESSAGE_LIST = {EXPIRED_ERROR_MESSAGE, CLAIMS_INVALID_MESSAGE,
            INVALID_TOKEN_MESSAGE, MALFORMED_JWT_MESSAGE, INCORRECT_TOKEN_FORMAT_MESSAGE, SIGNATURE_MESSAGE,
            UNSUPPORTED_MESSAGE, NO_BEARER_MESSAGE};

    // dev pattern url authorization
    private static final String[] AUTHORIZED_PATTERN_ARRAY_DEV = {PATTERN_PUBLIC_URL, V2_API_DOCS_URL,
            SWAGGER_RESOURCES_URL, SWAGGER_RESOURCES_PATTERN_URL, CONFIGURATION_UI_URL, CONFIGURATION_SECURITY_URL,
            SWAGGER_UI_HTML_URL, WEBJARS_URL, V3_API_DOCS_URL, SWAGGER_UI_PATTERN_URL, H2_CONSOLE_PATTERN_URL};

    // prod pattern url authorization
    private static final String[] AUTHORIZED_PATTERN_ARRAY = {PATTERN_PUBLIC_URL};

    public static String[] getErrorList() {
        return ERRORS_LIST;
    }

    public static String[] getErrorsMessageList() {
        return ERRORS_MESSAGE_LIST;
    }

    public static String[] getAuthorizedUrl(Boolean env) {
        if (Boolean.TRUE.equals(env)) {
            return AUTHORIZED_PATTERN_ARRAY_DEV;
        } else {
            return AUTHORIZED_PATTERN_ARRAY;
        }
    }

}
