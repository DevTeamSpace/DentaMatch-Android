package com.appster.dentamatch.util;

import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.network.response.auth.JobTitleList;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Created by gautambisht on 11/11/16.
 */

public final class PreferenceUtil {
    /*
    key of preference
     */
    private static final String KEY_APP_STATE = "APP_STATE";
    private static final String KEY_FIRST_TIME = "FIRST_TIME";
    private static final String KEY_ON_BOARDING = "ON_BOARDING";
    private static final String KEY_IS_LOGIN = "IS_LOGIN";
    private static final String KEY_FIRST_NAME = "FIRST_NAME";
    private static final String KEY_LAST_NAME = "LAST_NAME";
    private static final String KEY_OFFICE_NAME = "OFFICE_NAME";
    private static final String KEY_YEAR = "YEAR";
    private static final String KEY_MONTH = "MONTH";
    private static final String KEY_JOB_TITLE = "JOB_TITLE";
    private static final String KEY_JOB_TITLE_POSITION = "JOB_TITLE_POSITION";
    private static final String KEY_JOB_TITLE_LIST = "JOB_TITLE_LIST";
    private static final String KEY_USER_TOKEN = "USER_TOKEN";
    private static final String KEY_JOB_TITLE_ID = "JOB_TITLE_ID";
    private static final String KEY_PROFILE_IMAGE_PATH = "PROFILE_IMAGE_PATH";


    public static void saveAppState(Object state) {
        Hawk.put(KEY_APP_STATE, state);
    }

    public static Object getAppState() {
        return Hawk.get(KEY_APP_STATE);
    }


    public static boolean isFirstTimeLaunch() {
        return Hawk.get(KEY_FIRST_TIME, false);
    }

    public static void setFirstTimeLaunch(boolean value) {
        Hawk.put(KEY_FIRST_TIME, value);
    }

    public static boolean getIsOnBoarding() {
        return Hawk.get(KEY_ON_BOARDING, false);
    }

    public static void setIsOnBoarding(boolean value) {
        Hawk.put(KEY_ON_BOARDING, value);
    }

    public static void setProfileImagePath(String value) {
        Hawk.put(KEY_PROFILE_IMAGE_PATH, value);
    }

    public static String getProfileImagePath() {
        return Hawk.get(KEY_PROFILE_IMAGE_PATH);
    }

    public static void setJobTitleList(ArrayList<JobTitleList> value) {
        Hawk.put(KEY_JOB_TITLE_LIST, value);
    }

    public static ArrayList<JobTitleList> getJobTitleList() {
        return Hawk.get(KEY_JOB_TITLE_LIST);
    }


    public static void setJobTitle(String value) {
        Hawk.put(KEY_JOB_TITLE, value);
    }

    public static String getJobTitle() {
        return Hawk.get(KEY_JOB_TITLE);
    }

    public static void setJobTitlePosition(int value) {
        Hawk.put(KEY_JOB_TITLE_POSITION, value);
    }

    public static int getJobTitlePosition() {
        return Hawk.get(KEY_JOB_TITLE_POSITION);
    }

    public static void setJobTitleId(int value) {
        Hawk.put(KEY_JOB_TITLE_ID, value);
    }

    public static int getJobTitleId() {
        return Hawk.get(KEY_JOB_TITLE_ID);
    }

    public static void setFistName(String value) {
        Hawk.put(KEY_FIRST_NAME, value);
    }

    public static String getFirstName() {
        return Hawk.get(KEY_FIRST_NAME);
    }

    public static void setYear(int value) {

        Hawk.put(KEY_YEAR, value);
    }

    public static int getYear() {
        return Hawk.get(KEY_YEAR);
    }

    public static void setMonth(int value) {

        Hawk.put(KEY_MONTH, value);
    }

    public static int getMonth() {
        return Hawk.get(KEY_MONTH);
    }

    public static void setLastName(String value) {
        Hawk.put(KEY_LAST_NAME, value);
    }

    public static String getLastName() {
        return Hawk.get(KEY_LAST_NAME);
    }

    public static void setOfficeName(String value) {
        Hawk.put(KEY_OFFICE_NAME, value);
    }

    public static String getOfficeName() {
        return Hawk.get(KEY_OFFICE_NAME);
    }

    public static void setUserToken(String value) {
        Hawk.put(KEY_USER_TOKEN, value);
    }

    public static String getKeyUserToken() {
        return Hawk.get(KEY_USER_TOKEN);
    }


    public static boolean getIsLogined() {
        return Hawk.get(KEY_IS_LOGIN, false);
    }

    public static void setIsLogined(boolean value) {
        Hawk.put(KEY_IS_LOGIN, value);
    }

    public static void reset() {
        Hawk.deleteAll();
    }
}
