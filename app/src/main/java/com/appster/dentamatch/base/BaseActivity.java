/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.base;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.presentation.auth.LoginActivity;
import com.appster.dentamatch.presentation.common.ImageViewingActivity;
import com.appster.dentamatch.presentation.searchjob.SearchJobDataHelper;
import com.appster.dentamatch.presentation.tracks.TrackJobsDataHelper;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import java.io.File;

import dagger.android.support.DaggerAppCompatActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created for common handling and design-.
 */

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class);
    private static final int MY_PERMISSION_ACCESS_LOCATION = 101;
    private boolean mAlive;
    private boolean mActive;
    private ProgressDialog mProgressDialog;
    private Toast mToast;
    private static PermissionCallback permissionResult;

    abstract public String getActivityName();

    @NonNull
    public String getTextFromEditText(@NonNull EditText editText) {
        Editable text = editText.getText();
        if (text != null) {
            return text.toString().trim();
        }
        return "";
    }

    public void showSnackBar(String message) {
        Alert.showSnackBar(findViewById(android.R.id.content), message);
    }

    public void showSnackBar(String message, String buttonText, View.OnClickListener listener) {
        Alert.showSnackBar(findViewById(android.R.id.content), message, buttonText, listener);
    }

    public boolean isActive() {
        return mActive;
    }

    private boolean isAlive() {
        return mAlive;
    }

    protected void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        String AUTHORITY = "com.appster.dentamatch.provider";
        Uri photoUri = FileProvider.getUriForFile(this, AUTHORITY, file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
    }

    protected void getImageFromGallery() {
        Intent gIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gIntent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(gIntent, getString(R.string.title_select_file)),
                Constants.REQUEST_CODE.REQUEST_CODE_GALLERY);
    }


    @Override
    protected void onDestroy() {
        hideProgressBar();
        mAlive = false;
        Alert.dismissSnackBar();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActive = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mActive = true;
    }

    @Override
    protected void onStop() {
        mActive = false;
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAlive = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationUtils.REQUEST_CHECK_SETTINGS:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(LocationUtils.TAG);
                fragment.onActivityResult(requestCode, resultCode, data);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_ACCESS_LOCATION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        } else {

            if (grantResults.length == 0) {
                if (permissionResult != null) permissionResult.permissionsDenied();
                return;
            }

            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    if (permissionResult != null) permissionResult.permissionsDenied();
                    return;
                }
            }

            if (permissionResult != null) {
                permissionResult.permissionsGranted();
            }
        }
    }

    public boolean hasPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission);
    }

    public interface PermissionCallback {

        void permissionsGranted();

        void permissionsDenied();
    }

    protected void showProgressBar() {
        showProgressBar(null, null, null, 0);
    }

    public void showProgressBar(int delayTime) {
        showProgressBar(null, null, null, delayTime);
    }

    public void showProgressBar(String msg) {
        showProgressBar(null, msg, null, 0);
    }

    public void showProgressBar(String msg, int delayTime) {
        showProgressBar(null, msg, null, delayTime);
    }

    public void showProgressBar(final String title, final String msg, final View view, int delayTime) {
        if (mProgressDialog != null) {
            hideProgressBar();
        }
        if (isAlive()) {
            if (delayTime > 0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processToShowDialog();
                    }
                }, delayTime);
            } else {
                processToShowDialog();
            }
        }
    }

    public void processToShowDialog() {
        try {
            mProgressDialog = ProgressDialog.show(this, null, null);
            if (mProgressDialog.getWindow() != null) {
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mProgressDialog.setCancelable(false);
                mProgressDialog.setContentView(View.inflate(this, R.layout.progress_bar, null));
            }

        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

    }

    public void hideProgressBar() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            mProgressDialog = null;

        } catch (Exception x) {
            LogUtils.LOGE(TAG, x.getMessage());
        }
    }


    //Show toast message and cancel if any previous toast is displaying.

    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void hideKeyboard() {
        try {
            hideKeyboard(getCurrentFocus());

        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    public void hideKeyboard(View view) {
        try {
            if (view != null) {
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    public void pushFragment(BaseFragment fragment, Bundle args, ANIMATION_TYPE animationType) {
        try {
            if (fragment == null) {
                return;
            }

            if (args != null) {
                fragment.setArguments(args);
            }

            getSupportFragmentManager().executePendingTransactions();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (!fragment.isAdded()) {
                ft.replace(R.id.fragment_container, fragment);
                ft.commitAllowingStateLoss();
            }

        } catch (Exception x) {
            LogUtils.LOGE(TAG, x.getMessage());
        }
    }

    public void localLogOut() {
        clearBadge();
        String fcmToken = PreferenceUtil.getFcmToken();
        PreferenceUtil.reset();
        PreferenceUtil.setFcmToken(fcmToken);
        PreferenceUtil.setIsOnBoarding(true);
        Utils.clearAllNotifications(this);
        DBHelper.getInstance().clearDBData();
        TrackJobsDataHelper.getInstance().clearInstance();
        SearchJobDataHelper.getInstance().clearInstance();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.EXTRA_IS_LOGIN, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void clearBadge() {
        try {
            ContentValues cv = new ContentValues();
            cv.put("badgecount", 0);
            getContentResolver().update(Uri.parse("content://com.sec.badge/apps"), cv, "package=?", new String[]{getPackageName()});
            ShortcutBadger.applyCount(this, 0);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            ShortcutBadger.applyCount(this, 0);
        }

    }

    public enum ANIMATION_TYPE {
        SLIDE, FADE, DEFAULT, NONE
    }

    public void launchImageViewer(View v, String imageUrl) {
        try {
            Intent intent = new Intent(this, ImageViewingActivity.class);
            intent.putExtra(Constants.EXTRA_PIC, imageUrl);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, v, "picImage");
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }
}