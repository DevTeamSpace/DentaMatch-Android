package com.appster.dentamatch;

/**
 * Created by gautambisht on 10/11/16.
 */

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.appster.dentamatch.util.NetworkMonitor;
import com.google.firebase.FirebaseApp;
import com.orhanobut.hawk.Hawk;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

//import com.facebook.stetho.Stetho;
//import com.squareup.leakcanary.LeakCanary;

public class DentaApp extends MultiDexApplication {

    private static Context mAppContext;

    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this.getApplicationContext();
        NetworkMonitor.initialize(mAppContext);

//        if (BuildConfig.DEBUG) {
////            LeakCanary.install(this);
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .detectActivityLeaks()
//                    .penaltyLog()
//                    //.penaltyDeath()
//                    .build());
//            Stetho.initialize(
//                    Stetho.newInitializerBuilder(this)
//                            .enableDumpapp(
//                                    Stetho.defaultDumperPluginsProvider(this))
//                            .enableWebKitInspector(
//                                    Stetho.defaultInspectorModulesProvider(this))
//                            .build());
//        }

        // Shared preference initialize
        Hawk.init(mAppContext).build();
        NetworkMonitor.initialize(getApplicationContext());

        /*int buildVersion = PreferenceUtils.getBuildVersion();
        if (BuildConfig.VERSION_CODE > buildVersion) {
            //PreferenceUtils.saveAppConfiguration(null);
        }*/

        //Install CustomActivityOnCrash
        if (!BuildConfig.DEBUG) {
            CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
            // TODO: 10/11/16 Change with your Crash Screen
            //CustomActivityOnCrash.setErrorActivityClass(SomethingWentWrongActivity.class);
            CustomActivityOnCrash.install(this);
            // TODO: 10/11/16 Uncomment if using Crashylytics
            //Fabric.with(this, new Crashlytics());
        }

        // GoogleAnalytics initialize
        //AnalyticsHelper.prepareAnalytics(this);

        // Push Notification initialize
        FirebaseApp.initializeApp(mAppContext);
    }
}
