package com.appster.dentamatch.util;

import android.location.Location;

import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
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
    private static final String KEY_DEVICE_ID = "DEVICE_ID";
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
    private static final String KEY_SEARCH_JOB_TITLE_LIST = "SEARCH_JOB_TITLE_LIST";
    private static final String KEY_USER_TOKEN = "USER_TOKEN";
    private static final String KEY_JOB_TITLE_ID = "JOB_TITLE_ID";
    private static final String KEY_PROFILE_IMAGE_PATH = "PROFILE_IMAGE_PATH";
    private static final String KEY_IS_JOB_FILTER_CHANGED = "KEY_IS_JOB_FILTER_CHANGED";
    private static final String KEY_CHAT_USER_ID = "KEY_CHAT_USER_ID";

    private static final String KEY_JOB_FILTER_SET = "KEY_JOB_FILTER_SET";
    private static final String KEY_JOB_FILTER_REQUEST = "KEY_JOB_FILTER_REQUEST";
    private static final String KEY_USER_CURRENT_LOC = "KEY_USER_CURRENT_LOC";
    private static final String KEY_FCM_TOKEN = "FCM_TOKEN";
    private static final String KEY_PROFILE_COMPLETE = "KEY_PROFILE_COMPLETE";

    private static final String KEY_USER_MODEL = "USER_MODEL";

    public static void setUserCurrentLocation(Location location) {
        Hawk.put(KEY_USER_CURRENT_LOC, location);
    }

    public static void setProfileCompleted(boolean completed){
        Hawk.put(KEY_PROFILE_COMPLETE, completed);
    }

    public static boolean isProfileCompleted(){
        return Hawk.get(KEY_PROFILE_COMPLETE, false);
    }

    public static Object getUserCurrentLocation() {
        return Hawk.get(KEY_USER_CURRENT_LOC);
    }

    public static void saveAppState(Object state) {
        Hawk.put(KEY_APP_STATE, state);
    }

    public static void saveJobFilter(SearchJobRequest request) {
        Hawk.put(KEY_JOB_FILTER_REQUEST, request);
    }

    public static void setFilterChanged(boolean filterChanged) {
        Hawk.put(KEY_IS_JOB_FILTER_CHANGED, filterChanged);
    }

    public static boolean isFilterChanged() {
        return Hawk.get(KEY_IS_JOB_FILTER_CHANGED, false);
    }

    public static Object getJobFilter() {
        return Hawk.get(KEY_JOB_FILTER_REQUEST);
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

    public static void setJobFilter(boolean value) {
        Hawk.put(KEY_JOB_FILTER_SET, value);
    }

    public static boolean isJobFilterSet() {
        return Hawk.get(KEY_JOB_FILTER_SET, false);
    }

    public static String getProfileImagePath() {
        return Hawk.get(KEY_PROFILE_IMAGE_PATH);
    }

    public static void setJobTitleList(ArrayList<JobTitleListModel> value) {
        Hawk.put(KEY_JOB_TITLE_LIST, value);
    }

    public static String getFcmToken() {
        return Hawk.get(KEY_FCM_TOKEN);
    }

    public static void setFcmToken(String token) {
        Hawk.put(KEY_FCM_TOKEN, token);
    }

    public static void setSearchJobTitleList(ArrayList<JobTitleListModel> value) {
        Hawk.put(KEY_SEARCH_JOB_TITLE_LIST, value);
    }

    public static ArrayList<JobTitleListModel> getJobTitleList() {
        return Hawk.get(KEY_JOB_TITLE_LIST);
    }

    public static void setDeviceId(String value) {
        Hawk.put(KEY_DEVICE_ID, value);
    }

    public static String getDeviceId() {
        return Hawk.get(KEY_DEVICE_ID);
    }

    public static ArrayList<JobTitleListModel> getSearchJobTitleList() {
        return Hawk.get(KEY_SEARCH_JOB_TITLE_LIST);
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
        return Hawk.get(KEY_JOB_TITLE_POSITION, 0);
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

    public static void setUserModel(UserModel userModel) {
        Hawk.put(KEY_USER_MODEL, userModel);
    }

    public static UserModel getUserModel() {
        return Hawk.get(KEY_USER_MODEL);
    }

    public static void reset() {
        Hawk.deleteAll();
    }

    public static void setUserChatId(String userId) {
        Hawk.put(KEY_CHAT_USER_ID, userId);
    }

    public static String getUserChatId() {
        return Hawk.get(KEY_CHAT_USER_ID);
    }
}
