package com.appster.dentamatch.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.appster.dentamatch.DentaApp;

/*
    Utility for checking Network
 */
public class NetworkMonitor extends BroadcastReceiver {
    private static final String TAG = LogUtils.makeLogTag(NetworkMonitor.class);
    private static final int NETWORK_TYPE_FAST_3G = 2;
    private static final int NETWORK_TYPE_FAST_WIFI = 1;
    public static final int NETWORK_TYPE_NO_NETWORK = 4;
    private static final int NETWORK_TYPE_SLOW = 3;

    private static boolean mConnectionAvailable = false;

    private NetworkMonitor() {
    }

    public static void initialize() {
        checkNetworkConnectivity(DentaApp.getInstance());
    }

    /**
     * To check network connectivity availability
     *
     * @param context context
     */
    private static void checkNetworkConnectivity(Context context) {
        try {
            mConnectionAvailable = false;
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                mConnectionAvailable = false;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectivity.getAllNetworks();
                    for (Network mNetwork : networks) {
                        NetworkInfo networkInfo = connectivity.getNetworkInfo(mNetwork);
                        if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            mConnectionAvailable = true;
                            break;
                        }
                    }
                } else {
                    NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();

                    if (activeNetwork != null) {
                        mConnectionAvailable = true;
                    }
                }
            }
        } catch (Exception e) {
            mConnectionAvailable = false;
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    /**
     * To check network type availability or strength
     *
     * @return Network current state
     */
    public static NetState getNetwork() {
        int i = getNetworkType();
        switch (i) {
            case NETWORK_TYPE_SLOW:
                return NetState.NET_2G;
            case NETWORK_TYPE_FAST_3G:
                return NetState.NET_3G;
            case NETWORK_TYPE_FAST_WIFI:
                return NetState.NET_WIFI;
            default:
                return NetState.NET_UNKNOWN;
        }
    }

    /**
     * To check network type availability
     *
     * @return wifi or mobile data service
     */
    private static int getNetworkType() {
        if (DentaApp.getInstance() != null) {
            ConnectivityManager connectivitymanager = (ConnectivityManager) DentaApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivitymanager != null) {
                NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();

                if (networkinfo != null && networkinfo.isConnected()) {
                    return 1;
                }
            }
            TelephonyManager telephonyManager = (TelephonyManager) DentaApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            int i = 0;
            if (telephonyManager != null)
                i = telephonyManager.getNetworkType();

            if (i > 0 && i < 3) {
                return 3;
            }

            return i <= 2 ? 4 : 2;
        } else {

            return -1;
        }
    }

    public void onReceive(Context context, Intent intent) {
        checkNetworkConnectivity(context);
    }

    public enum NetState {
        NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

}
