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

    /**
     * --- String constants ---
     **/
    /* format string */
    public static final String ERROR_MANAGEMENT = "{} : {}";


    /* errors keys strings */
    public static final String ERROR = "Error";
    public static final String UNAUTHORIZED = "Unauthorized";

    public static final String EXPIRED = "Expired";
    public static final String CLAIMS_INVALID = "Invalid";
    public static final String MALFORMED = "Malformed";
    public static final String INCORRECT_TOKEN_FORMAT = "FormatError";
    public static final String SIGNATURE = "SignatureError";
    public static final String UNSUPPORTED = "UnsupportedError";

    public static final String ERROR_ROLE = "Error: role not found. ";
    public static final String ERROR_USERNAME_TAKEN = "Error : Username is already taken. ";
    public static final String ERROR_MAIL_TAKEN = "Error : Email is already use. ";

    public static final String INVALID_TOKEN_MESSAGE = "Token is invalid. ";
    public static final String INVALID_REFRESH = "Refresh token is invalid. ";

    /* errors message strings */
    public static final String EXPIRED_ERROR_MESSAGE = EXPIRED + " - JWT Token has expired. ";
    public static final String CLAIMS_INVALID_MESSAGE = CLAIMS_INVALID + " - Data stored in jwt token are invalid. ";
    public static final String INCORRECT_TOKEN_FORMAT_MESSAGE = INCORRECT_TOKEN_FORMAT + " - The token can't be parsed. ";
    public static final String MALFORMED_JWT_MESSAGE = MALFORMED + " - JWT was not correctly constructed. ";
    public static final String SIGNATURE_MESSAGE = SIGNATURE + " - Can't verify the JWT signature. ";
    public static final String UNSUPPORTED_MESSAGE = UNSUPPORTED + " - The jwt is in an unsupported format. ";


    /* tokens strings */
    public static final String HEADER_TOKEN = "Authorization";
    public static final String TOKEN_START = "Bearer ";
    public static final String TOKEN_START_REFRESH = "Refresh ";
    public static final String REFRESH_TOKEN = "Refresh";
    public static final String STATUS = "Status";
    public static final String SUCCESS = "Success";
    public static final String MESSAGE = "Message";
    public static final String PATH = "Path";


    /**
     * --- number constants ---
     **/
    public static final int BEARER_SUBSTRING = 7;
    public static final int REFRESH_SUBSTRING = 8;


    private static final String[] ERRORS_LIST = {EXPIRED, CLAIMS_INVALID, MALFORMED, INCORRECT_TOKEN_FORMAT, SIGNATURE, UNSUPPORTED};
    private static final String[] ERRORS_MESSAGE_LIST = {EXPIRED_ERROR_MESSAGE, CLAIMS_INVALID_MESSAGE, INVALID_TOKEN_MESSAGE,
            MALFORMED_JWT_MESSAGE, INCORRECT_TOKEN_FORMAT_MESSAGE, SIGNATURE_MESSAGE, UNSUPPORTED_MESSAGE};

    public static String[] getErrorList() {
        return ERRORS_LIST;
    }

    public static String[] getErrorsMessageList() {
        return ERRORS_MESSAGE_LIST;
    }
}
