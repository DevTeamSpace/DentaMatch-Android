package com.appster.dentamatch.chat;

import android.content.Context;
import android.content.pm.ProviderInfo;

import com.appster.dentamatch.util.LogUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class RealmManager {
    private static String TAG = "RealmManager";
    static Realm realm;

    static RealmConfiguration realmConfiguration;
    private static int openCounter = 0;
    private static int closeCounter = 0;

    public static void initializeRealmConfig(Context appContext) {
        Realm.init(appContext);

        if (realmConfiguration == null) {
            RealmConfiguration realmConfiguration = new RealmConfiguration
                    .Builder()
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            setRealmConfiguration(realmConfiguration);

        }
    }

    private static void setRealmConfiguration(RealmConfiguration realmConfiguration) {
        RealmManager.realmConfiguration = realmConfiguration;
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static Realm getRealm() {
        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
            openCounter++;
            LogUtils.LOGD(TAG, "while opening-->open counter-->" + openCounter + " close counter--->" + closeCounter);
        }

        return realm;
    }

    public static void releaseRealm() {
        Realm.compactRealm(realmConfiguration);
        closeRealmInstance(realm);
        LogUtils.LOGD(TAG, " ending releasing--> open counter-->" + openCounter + " close counter--->" + closeCounter);
        openCounter = 0;
        closeCounter = 0;
    }

    public static Realm getRealmNewInstance(Context context) {
        openCounter++;
        LogUtils.LOGD(TAG, "while opening for thread-->open counter-->" + openCounter + " close counter--->" + closeCounter);

        return Realm.getDefaultInstance();
    }


    private static void closeRealmInstance(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
            realm = null;
            closeCounter++;
            LogUtils.LOGD(TAG, "while closing-->open counter-->" + openCounter + " close counter--->" + closeCounter);
        }
    }


}
