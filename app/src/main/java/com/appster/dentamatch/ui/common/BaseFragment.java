/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appster.dentamatch.util.LogUtils;

/**
 * Created by gautambisht on 11/11/16.
 * To inject activity reference.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class);
    protected Handler handler = new Handler();
    private boolean active;
    private boolean alive;
    private Toast toast;

    public abstract String getFragmentName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alive = true;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alive = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    private void hideKeyboard() {
        Activity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void hideKeyboard(View view) {
        Activity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void showToast(String message) {

        if (getActivity() != null && message != null) {

            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void showToastLengthLong(String message) {

        if (getActivity() != null && message != null) {

            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        alive = false;
        super.onDestroy();
    }

    public void showSnackBar(String message) {
        getBaseActivity().showSnackBar(message);
    }

    public void showSnackBar(String message, String buttonText, View.OnClickListener listener) {
        getBaseActivity().showSnackBar(message, buttonText, listener);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected boolean isAlive() {
        return alive;
    }

    protected boolean isActive() {
        return active;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
        active = false;
    }

    private void showProgressBar(String title, String message, View view, int delayTime) {
        try {
            if (isAlive() && getBaseActivity() != null) {
                getBaseActivity().showProgressBar(title, message, view, delayTime);
            }

        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    protected void showProgressBar() {
        showProgressBar(null, null, null, 0);
    }

    protected void showProgressBar(String msg) {
        showProgressBar(null, msg, null, 0);
    }

    protected void showProgressBar(int delayTime) {
        showProgressBar(null, null, null, delayTime);
    }

    public void showProgressBar(String msg, int delayTime) {
        showProgressBar(null, msg, null, delayTime);
    }

    protected void launchImageViewer(View v, String imageUrl) {
        try {
            getBaseActivity().launchImageViewer(v, imageUrl);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

}
