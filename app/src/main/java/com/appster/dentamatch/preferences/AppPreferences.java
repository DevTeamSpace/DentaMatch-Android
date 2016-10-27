package com.appster.dentamatch.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.appster.dentamatch.network.ApiResponse;

/**
 * Written by Appster on 14/04/16.
 */
public class AppPreferences {
    private static final String PREFS_NAME = "com.appster.ondemand";
    public static final String PREFS_EMAIL_SEEKER = "com.appster.ondemand.seeker";
    public static final String PREFS_EMAIL_PROVIDER = "com.appster.ondemand.provider";
    private static AppPreferences sInstance = null;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    @SuppressLint("CommitPrefEdits")
    private AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreferences(context);
        }
    }
    public static synchronized AppPreferences getInstance() {
        if (sInstance == null)
            throw new IllegalStateException(AppPreferences.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        return sInstance;
    }

    public void setStringPreference(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getStringPreference(String key) {
        return sharedPrefs.getString(key, "");
    }

    public void setLongPreference(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public long getLongPreference(String key) {
        return sharedPrefs.getLong(key, 0);
    }


    public void setBooleanPreference(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    //    retrieve boolean value
    public boolean getBooleanPreference(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public void setIntegerPreference(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public int getIntegerPreference(String key) {
        return sharedPrefs.getInt(key, 0);
    }

    public void removePreferences() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public void clearPrefs(){
    }

    public void setLoginPreferences(ApiResponse response) {
    }
}
