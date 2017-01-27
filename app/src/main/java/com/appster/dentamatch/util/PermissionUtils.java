package com.appster.dentamatch.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Virender on 26/04/16.
 */
public class PermissionUtils {
    public static boolean checkPermissionGranted(String permission, Activity mActivity) {
        boolean Status = false;
        Status = ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;

        return Status;
    }
    public static void requestPermission(Activity mActivity,String[] permission,int requestCode){
        ActivityCompat.requestPermissions(mActivity, permission,requestCode);

    }
}

