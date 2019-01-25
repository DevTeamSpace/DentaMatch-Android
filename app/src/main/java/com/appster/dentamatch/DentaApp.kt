package com.appster.dentamatch

import android.os.StrictMode
import com.appster.dentamatch.chat.DBHelper
import com.appster.dentamatch.di.DaggerApplicationComponent
import com.appster.dentamatch.util.NetworkMonitor
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.orhanobut.hawk.Hawk
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric

class DentaApp : DaggerApplication() {

    companion object {
        var appContext: DentaApp? = null
        fun getInstance(): DentaApp? = appContext
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerApplicationComponent.builder().create(this)

    private var mixpanelAPI: MixpanelAPI? = null

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Fabric.with(this, Crashlytics())
        mixpanelAPI = MixpanelAPI.getInstance(this, getString(R.string.mixpanel_token))
        Instabug.Builder(this, "6dea212496faf4e49dacc262e6e4f593")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build()
        DBHelper.getInstance().initializeRealmConfig(this)
        Hawk.init(this).build()
        NetworkMonitor.initialize()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            //LeakCanary.install(this);
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .build())
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build())
        }
    }

    fun getMixpanelAPI(): MixpanelAPI? = mixpanelAPI
}