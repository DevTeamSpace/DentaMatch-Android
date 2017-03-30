package com.appster.dentamatch;

/**
 * Created by gautambisht on 10/11/16.
 */

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.util.NetworkMonitor;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.orhanobut.hawk.Hawk;

import io.fabric.sdk.android.Fabric;


public class DentaApp extends MultiDexApplication {
    private static DentaApp mAppContext;
    public static int NOTIFICATION_COUNTER=0;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mAppContext = this;

        /**
         * Initialize DB Helper class.
         */
        DBHelper.getInstance().initializeRealmConfig(mAppContext);
        Hawk.init(mAppContext).build();
        NetworkMonitor.initialize();

        // Push ReadNotificationRequest initialize
        FirebaseApp.initializeApp(mAppContext);
    }

    public static DentaApp getInstance(){
        return mAppContext;
    }

}
