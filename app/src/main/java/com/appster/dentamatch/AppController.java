package com.appster.dentamatch;

import android.app.Application;
import android.content.Context;

import com.appster.dentamatch.preferences.AppPreferences;
import com.appster.dentamatch.utils.Util;

/**
 * Created by Appster on 15/02/16.
 */
public class AppController extends Application{

    private static final String TAG = "AppController";
    private static AppController mInstance;
    public static String DEVICE_ID;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());

//        MultiDex.install(this);
        mInstance = this;

        DEVICE_ID = Util.getDeviceID(this);

        // Initialize preference manager.
        AppPreferences.initializeInstance(getApplicationContext());

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
