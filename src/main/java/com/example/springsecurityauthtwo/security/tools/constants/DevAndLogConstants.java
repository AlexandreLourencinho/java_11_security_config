package com.example.springsecurityauthtwo.security.tools.constants;

public final class DevAndLogConstants {

    private DevAndLogConstants() {
    }

    /* -------------------------------------- Logs messages constants --------------------------------------- */
    public static final String DEV_FILTER_CHAIN_LOG = "Development or Test environment filter chain initialized";
    public static final String PROD_FILTER_CHAIN_LOG = "production environment filter chain initialized";


    /* ---------------------------------------- profiles ---------------------------------------- */ //
    public static final String DEV_ENV = "dev";
    public static final String TEST_ENV = "test";


    /* ---------------------------------------- format string ---------------------------------------- */
    public static final String ERROR_MANAGEMENT = "{} : {}";
}
