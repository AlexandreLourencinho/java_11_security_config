package com.example.springsecurityauthtwo.security.tools.constants;

public final class ErrorConstants {

    private ErrorConstants() {
    }

    /* Entities error*/
    public static final String ERROR_ROLE = "Error: role not found. ";
    public static final String ERROR_USERNAME_TAKEN = "Error : Username is already taken. ";
    public static final String ERROR_MAIL_TAKEN = "Error : Email is already use. ";
    public static final String USER_NOT_FOUND = "Error: user not found";

    /* error keys */
    public static final String ERROR = "Error";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String EXPIRED = "Expired";
    public static final String CLAIMS_INVALID = "Invalid";
    public static final String MALFORMED = "Malformed";
    public static final String INCORRECT_TOKEN_FORMAT = "FormatError";
    public static final String SIGNATURE = "SignatureError";
    public static final String UNSUPPORTED = "UnsupportedError";
    public static final String NO_BEARER = "NoBearerError";
    public static final String INTERNAL_SERVER_ERROR = "InternalError";

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

    /* ---------------------------------------- constants arrays ---------------------------------------- */
    /* errors list */
    private static final String[] ERRORS_LIST = {EXPIRED, CLAIMS_INVALID, MALFORMED, INCORRECT_TOKEN_FORMAT, SIGNATURE,
            UNSUPPORTED, NO_BEARER, INTERNAL_SERVER_ERROR};

    /* error messages list */
    private static final String[] ERRORS_MESSAGE_LIST = {EXPIRED_ERROR_MESSAGE, CLAIMS_INVALID_MESSAGE,
            INVALID_TOKEN_MESSAGE, MALFORMED_JWT_MESSAGE, INCORRECT_TOKEN_FORMAT_MESSAGE, SIGNATURE_MESSAGE,
            UNSUPPORTED_MESSAGE, NO_BEARER_MESSAGE, INTERNAL_SERVER_ERROR_MESSAGE};

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


}
