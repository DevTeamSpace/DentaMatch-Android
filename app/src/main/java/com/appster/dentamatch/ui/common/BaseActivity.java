package com.appster.dentamatch.ui.common;

import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.ui.auth.LoginActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import java.io.File;

/**
 * Created by gautambisht on 11/11/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BASE_ACTIVITY";
    private final String AUTHORITY = "com.appster.dentamatch.provider";
    protected static final int MY_PERMISSION_ACCESS_LOCATION = 101;
    protected boolean mAlive;
    private boolean mActive;
    private ProgressDialog mProgressDialog;
    private Toast mToast;
    public static PermissionCallback permissionResult;

    public static BaseFragment getFragment(Constants.FRAGMENTS fragmentId) {
        BaseFragment fragment = null;

        switch (fragmentId) {
            case TEST_FRAGMENT:
                break;
        }

        return fragment;
    }

    abstract public String getActivityName();

    public void showSnackBar(String message) {
        Alert.showSnackBar(findViewById(android.R.id.content), message);
    }

    public void showSnackBar(String message, String buttonText, View.OnClickListener listener) {
        Alert.showSnackBar(findViewById(android.R.id.content), message, buttonText, listener);
    }

    public boolean isActive() {
        return mActive;
    }

    public boolean isAlive() {
        return mAlive;
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        Uri photoUri = FileProvider.getUriForFile(this, AUTHORITY, file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE.REQUEST_CODE_CAMERA);
    }

    public void getImageFromGallery() {
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

            default: break;
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

            if (permissionResult != null){
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

    public void showProgressBar() {
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
            mProgressDialog =  ProgressDialog.show(this,null,null);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(View.inflate(this, R.layout.progress_bar, null));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hideProgressBar() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            mProgressDialog = null;

        } catch (Exception x) {
            x.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {
        try {
            if (view != null) {
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushFragment(BaseFragment fragment, Bundle args, ANIMATION_TYPE animationType) {
        try {
            if (fragment == null) return;
            if (args != null)
                fragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (animationType) {
                case DEFAULT:
                case SLIDE:
                    ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    break;
                case FADE:
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                    break;
                case NONE:
                    break;
            }
            if (fragment.isAdded()) {

            } else {
                ft.replace(R.id.fragment_container, fragment);
                ft.commitAllowingStateLoss();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void localLogOut() {

        String fcmToken= PreferenceUtil.getFcmToken();
        PreferenceUtil.reset();
        PreferenceUtil.setFcmToken(fcmToken);
        PreferenceUtil.setIsOnBoarding(true);
        Utils.clearAllNotifications(this);
        DBHelper.getInstance().clearDBData();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.EXTRA_IS_LOGIN, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public enum ANIMATION_TYPE {
        SLIDE, FADE, DEFAULT, NONE
    }
    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
