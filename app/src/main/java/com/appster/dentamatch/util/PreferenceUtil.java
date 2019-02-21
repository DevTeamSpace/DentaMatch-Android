/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.util;

import android.location.Location;
import android.support.annotation.Nullable;

import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * To store and fetch data in local application private level shared preference data
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

    private static final String KEY_PREFERRED_JOB_LOCATION_NAME = "PREFERRED_JOB_LOCATION_NAME";
    private static final String KEY_PREFERRED_JOB_LOCATION_ID = "PREFERRED_JOB_LOCATION_ID";
    private static final String KEY_LICENSE_NUMBER = "LICENSE_NUMBER";

    private static final String KEY_JOB_SEEKER_VERIFIED = "isJobSeekerVerified";
    private static final String KEY_USER_VERIFIED = "isVerified";

    private static final String KEY_SET_AVAILABILITY = "keySetAvailability";
    private static final String KEY_PREFERRED_JOB_LIST = "PREFERRED_JOB_LIST";


    /**
     * To save the current location
     *
     * @param location gps location
     */
    public static void setUserCurrentLocation(Location location) {
        Hawk.put(KEY_USER_CURRENT_LOC, location);
    }

    /**
     * To save profile completed flag so that if the app killed in SignUp transit it can be handle later.
     *
     * @param completed boolean
     */
    public static void setProfileCompleted(boolean completed) {
        Hawk.put(KEY_PROFILE_COMPLETE, completed);
    }

    /**
     * To save the filter state applied by user so that later he would be getting same filter applied
     *
     * @param request filter model
     */
    public static void saveJobFilter(SearchJobRequest request) {
        Hawk.put(KEY_JOB_FILTER_REQUEST, request);
    }

    /**
     * Save flag to check the change in filter
     *
     * @param filterChanged boolean
     */
    public static void setFilterChanged(boolean filterChanged) {
        Hawk.put(KEY_IS_JOB_FILTER_CHANGED, filterChanged);
    }

    /**
     * To check if the filter been changed nor not
     *
     * @return boolean
     */
    public static boolean isFilterChanged() {
        return Hawk.get(KEY_IS_JOB_FILTER_CHANGED, false);
    }

    /**
     * To get applied filter object
     *
     * @return Object
     */
    public static Object getJobFilter() {
        return Hawk.get(KEY_JOB_FILTER_REQUEST);
    }

    /**
     * To check and launch On Boarding tutorial
     *
     * @return boolean
     */
    public static boolean getIsOnBoarding() {
        return Hawk.get(KEY_ON_BOARDING, false);
    }

    /**
     * To set On Boarding flag status for the next time launch
     *
     * @param value boolean
     */
    public static void setIsOnBoarding(boolean value) {
        Hawk.put(KEY_ON_BOARDING, value);
    }

    /**
     * To set profile image path so that next time if avvailable we can fetch the same for fast loading
     *
     * @param value path
     */
    public static void setProfileImagePath(String value) {
        Hawk.put(KEY_PROFILE_IMAGE_PATH, value);
    }

    /**
     * @param value
     */
    public static void setJobFilter(boolean value) {
        Hawk.put(KEY_JOB_FILTER_SET, value);
    }

    /**
     * @return
     */
    public static String getProfileImagePath() {
        return Hawk.get(KEY_PROFILE_IMAGE_PATH);
    }

    /**
     * @param value
     */
    public static void setJobTitleList(ArrayList<JobTitleListModel> value) {
        Hawk.put(KEY_JOB_TITLE_LIST, value);
    }

    /**
     * To get FCM token
     *
     * @return FCM token
     */
    public static String getFcmToken() {
        return Hawk.get(KEY_FCM_TOKEN);
    }

    /**
     * To save FCM token
     *
     * @param token FCM token
     */
    public static void setFcmToken(String token) {
        Hawk.put(KEY_FCM_TOKEN, token);
    }

    /**
     * Save searched job title list
     *
     * @param value list of job title model
     */
    public static void setSearchJobTitleList(ArrayList<JobTitleListModel> value) {
        Hawk.put(KEY_SEARCH_JOB_TITLE_LIST, value);
    }

    /**
     * Get list of save job title list
     *
     * @return list of job title
     */
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

    public static void setJobTitleId(Integer value) {
        Hawk.put(KEY_JOB_TITLE_ID, value);
    }

    public static int getJobTitleId() {
        return Hawk.get(KEY_JOB_TITLE_ID, 0);
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

    @Nullable
    public static Integer getYear() {
        return Hawk.get(KEY_YEAR);
    }

    public static void setMonth(int value) {

        Hawk.put(KEY_MONTH, value);
    }

    @Nullable
    public static Integer getMonth() {
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


    public static boolean getIsLogin() {
        return Hawk.get(KEY_IS_LOGIN, false);
    }

    public static void setIsLogin(boolean value) {
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


    public static void setPreferredJobLocationName(String value) {
        Hawk.put(KEY_PREFERRED_JOB_LOCATION_NAME, value);
    }

    public static String getPreferredJobLocationName() {
        return Hawk.get(KEY_PREFERRED_JOB_LOCATION_NAME);
    }


    public static void setPreferredJobLocationID(int value) {
        Hawk.put(KEY_PREFERRED_JOB_LOCATION_ID, value);
    }

    public static int getPreferredJobLocationId() {
        return Hawk.get(KEY_PREFERRED_JOB_LOCATION_ID, 0);
    }


    public static void setLicenseNumber(String value) {
        Hawk.put(KEY_LICENSE_NUMBER, value);
    }

    public static String getLicenseNumber() {
        return Hawk.get(KEY_LICENSE_NUMBER);
    }


    public static void setKeyJobSeekerVerified(int value) {
        Hawk.put(KEY_JOB_SEEKER_VERIFIED, value);
    }

    public static int getKeyJobSeekerVerified() {
        return Hawk.get(KEY_JOB_SEEKER_VERIFIED);
    }


    public static void setUserVerified(int value) {
        Hawk.put(KEY_USER_VERIFIED, value);
    }

    public static int getUserVerified() {
        return Hawk.get(KEY_USER_VERIFIED, 0);
    }


    public static void setSetAvailability(boolean value) {
        Hawk.put(KEY_SET_AVAILABILITY, value);
    }

    public static boolean getAvailability() {
        return Hawk.get(KEY_SET_AVAILABILITY, false);
    }


    public static ArrayList<PreferredJobLocationData> getPreferredJobList() {
        return Hawk.get(KEY_PREFERRED_JOB_LIST);
    }

    public static void setPreferredJobList(ArrayList<PreferredJobLocationData> value) {
        Hawk.put(KEY_PREFERRED_JOB_LIST, value);
    }

}
