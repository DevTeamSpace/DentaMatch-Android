package com.appster.dentamatch.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * To handle runtime permission
 */
public class PermissionUtils {

    /**
     * To check if the requested permission has been granted or not
     *
     * @param permission runtime permission
     * @param mActivity  requested in activity
     * @return permission granted status
     */
    public static boolean checkPermissionGranted(String permission, Activity mActivity) {
        boolean Status;
        Status = ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;

        return Status;
    }

    /**
     * Tp request runtime permission
     *
     * @param mActivity   requested in activity
     * @param permission  permissions
     * @param requestCode request code
     */
    public static void requestPermission(Activity mActivity, String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, permission, requestCode);
    }
}

