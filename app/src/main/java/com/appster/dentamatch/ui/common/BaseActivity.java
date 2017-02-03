package com.appster.dentamatch.ui.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;

/**
 * Created by gautambisht on 11/11/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BASE_ACTIVITY";
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
                //fragment = new TestFragment();
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
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (permissionResult != null) permissionResult.permissionsDenied();
                    return;
                }
            }
            if (permissionResult != null) permissionResult.permissionsGranted();
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
                        processToShowDialog(title, msg, view);
                    }
                }, delayTime);
            } else {
                processToShowDialog(title, msg, view);
            }
        }
    }

    public void processToShowDialog(String title, String msg, View view) {
        try {
            mProgressDialog = ProgressDialog.show(new ContextThemeWrapper(BaseActivity.this,
                    android.R.style.Theme_Holo_Light), title, msg, true, false);
            if (view != null)
                mProgressDialog.setContentView(view);
            // Change as per your view
            /*else
                mProgressDialog.setContentView(R.layout.progress_view);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressBar() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        } catch (Exception x) {
        }
    }


    //Show toast message and cancel if any previous toast is displaying.

    public void showToast(String message) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void hideKeyboard() {
        try {
            hideKeyboard(getCurrentFocus());
        } catch (Exception e) {
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



    private void doLogin(String email, String password) {
        showProgressBar();
        /*LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailId(email);
        loginRequest.setPassword(password);
        Call<LoginResponse> call = RequestController.createService(AuthWebServices.class, true)
                .userAuthenticate(loginRequest);
        call.enqueue(new BaseCallback<LoginResponse>(this) {
            @Override
            public void onSuccess(LoginResponse response) {
                hideHud();
                if (response.isSuccess()) {
                    finish();
                } else {
                    showToast(TextUtils.isEmpty(response.getMessage()) ? getString(R.string.error_network_request)
                            : response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call) {
                hideHud();
                //showToast(getString(R.string.error_network_request));
            }
        });*/
    }


    public void logOut() {

//        AuthWebServices client = RequestController.createService(AuthWebServices.class, true);
//        Call<BaseResponse> response = client.logout();
//        showProgressBar();
//        response.enqueue(new BaseCallback<BaseResponse>(this) {
//            @Override
//            public void onSuccess(BaseResponse response) {
//                //LogUtils.LOGD(TAG, "login Success" + response.getResult().getUserdetails().getGmailid());
//                Utils.logOut(BaseActivity.this);
//            }
//
//            @Override
//            public void onFail(Call<BaseResponse> call, BaseResponse response1) {
//
//            }
//        });


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
