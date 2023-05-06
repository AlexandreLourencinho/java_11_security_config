package com.example.springsecurityauthtwo.security.tools.utils;

import static com.example.springsecurityauthtwo.security.tools.constants.UrlAndPatternConstants.getAuthorizedUrl;
import static com.example.springsecurityauthtwo.security.tools.constants.UrlAndPatternConstants.getAuthorizedUrlDev;
import static com.example.springsecurityauthtwo.security.tools.constants.UrlAndPatternConstants.getAuthorizedPattern;
import static com.example.springsecurityauthtwo.security.tools.constants.UrlAndPatternConstants.getAuthorizedPatternDev;

public final class ConstantsUtils {

    private ConstantsUtils() {
    }

    public static final String DEV_ENV = "dev";
    public static final String TEST_ENV = "test";

    /**
     * check the active profile
     *
     * @param profile the current active profile
     * @return a boolean, true is profile is dev or env, false otherwise
     */
    public static Boolean isDevOrTestEnv(String profile) {
        return (profile.equals(DEV_ENV) || profile.equals(TEST_ENV));
    }

    /**
     * get the authorized pattern for dev/test env or prod env
     *
     * @param isDevEnv a boolean, true if env is dev or test
     * @return an array of string containing the authorized pattern
     */
    public static String[] utilsGetAuthorizedUrlPattern(Boolean isDevEnv) {
        if (Boolean.TRUE.equals(isDevEnv)) {
            return getAuthorizedPatternDev();
        } else {
            return getAuthorizedPattern();
        }
    }

    /**
     * get the authorized url depending on the profile
     *
     * @param isDevEnv a boolean, true if env is dev or test
     * @return an array of string, containing the authorized url
     */
    public static String[] utilsGetAuthorizedUrl(Boolean isDevEnv) {
        if (Boolean.TRUE.equals(isDevEnv)) {
            return getAuthorizedUrlDev();
        } else {
            return getAuthorizedUrl();
        }
    }

}
