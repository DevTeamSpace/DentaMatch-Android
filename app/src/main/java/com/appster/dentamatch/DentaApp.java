package com.appster.dentamatch;

/**
 * Created by gautambisht on 10/11/16.
 */

import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.util.NetworkMonitor;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.orhanobut.hawk.Hawk;

import io.fabric.sdk.android.Fabric;


public class DentaApp extends MultiDexApplication {
    private static DentaApp mAppContext;
    //public static int NOTIFICATION_COUNTER=0;
    private MixpanelAPI mixpanelAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mAppContext = this;
        mixpanelAPI = MixpanelAPI.getInstance(this, getString(R.string.mixpanel_token));

       new Instabug.Builder(this, "6dea212496faf4e49dacc262e6e4f593")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();
        /*
          Initialize DB Helper class.
         */
        DBHelper.getInstance().initializeRealmConfig(mAppContext);
        Hawk.init(mAppContext).build();
        NetworkMonitor.initialize();

        // Push ReadNotificationRequest initialize
        FirebaseApp.initializeApp(mAppContext);


        if (BuildConfig.DEBUG) {
           // LeakCanary.install(this);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build());
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }

    public static DentaApp getInstance(){
        return mAppContext;
    }

    public MixpanelAPI getMixpanelAPI(){
        return mixpanelAPI;
    }

}
