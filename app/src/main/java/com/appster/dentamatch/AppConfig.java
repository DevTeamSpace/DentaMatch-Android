package com.appster.dentamatch;

/**
 * Application configurations.
 */
public class AppConfig {
    public static final int NUMBER_OF_PAGES = 3;
    public static final long SERVER_TIME_OUT = 2000;

    private AppConfig() {
        throw new IllegalAccessError("Utility class");
    }
}