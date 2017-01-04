package com.appster.dentamatch.util;

import com.orhanobut.hawk.Hawk;

/**
 * Created by gautambisht on 11/11/16.
 */

public final class PreferenceUtil {

    private static final String KEY_APP_STATE = "APP_STATE";
    private static final String KEY_JOY_RIDE = "JOY_RIDE";
    private static final String KEY_FIRST_TIME = "FIRST_TIME";

    private PreferenceUtil() {

    }

    public static void saveAppState(Object state) {
        Hawk.put(KEY_APP_STATE, state);
    }

    public static Object getAppState() {
        return Hawk.get(KEY_APP_STATE);
    }

    public static boolean isJoyRideComplete() {
        return Hawk.get(KEY_JOY_RIDE, false);
    }

    public static void setJoyRideComplete(boolean value) {
        Hawk.put(KEY_JOY_RIDE, value);
    }

    public static boolean isFirstTimeLaunch() {
        return Hawk.get(KEY_FIRST_TIME, false);
    }

    public static void setFirstTimeLaunch(boolean value) {
        Hawk.put(KEY_FIRST_TIME, value);
    }

    public static void reset() {
        Hawk.deleteAll();
    }
}
