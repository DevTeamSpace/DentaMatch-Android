package com.appster.dentamatch.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by gautambisht on 11/11/16.
 */

public abstract class BaseFragment extends Fragment {

    protected Handler handler = new Handler();
    protected boolean active;
    protected boolean alive;
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

    protected void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void hideKeyboard(View view) {
        if (getActivity() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view != null) {
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

    public boolean isAlive() {
        return alive;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
        active = false;
    }

    protected void showProgressBar(String title, String message, View view, int delayTime) {
        try {
            if (isAlive() && getBaseActivity() != null) {
                getBaseActivity().showProgressBar(title, message, view, delayTime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showProgressBar() {
        showProgressBar(null, null, null, 0);
    }

    public void showProgressBar(String msg) {
        showProgressBar(null, msg, null, 0);
    }

    protected void showProgressBar(int delayTime) {
        showProgressBar(null, null, null, delayTime);
    }

    public void showProgressBar(String msg, int delayTime) {
        showProgressBar(null, msg, null, delayTime);
    }

}
