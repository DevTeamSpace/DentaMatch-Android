package com.appster.dentamatch.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.auth.LoginActivity;
import com.appster.dentamatch.ui.onboardtutorial.OnBoardingActivity;
import com.appster.dentamatch.ui.searchjob.SearchJobActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.google.firebase.iid.FirebaseInstanceId;

/*
 * Written by Appster on 14/04/16.
 */
public class SplashActivity extends Activity implements Runnable {
    private static final int SPLASH_TIME = 2000;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        manageFirebaseDeviceToken();
        handler.postDelayed(SplashActivity.this, SPLASH_TIME);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        if (PreferenceUtil.getIsOnBoarding()) {

            if (PreferenceUtil.getIsLogin()) {

                if (!PreferenceUtil.isJobFilterSet()) {
                    startActivity(new Intent(SplashActivity.this, SearchJobActivity.class)
                            .putExtra(Constants.EXTRA_IS_FIRST_TIME, true));

                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                }

            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
        }

        finish();
    }


    private void manageFirebaseDeviceToken() {
        String localToken = PreferenceUtil.getFcmToken();

        if (TextUtils.isEmpty(localToken)) {
            String token = FirebaseInstanceId.getInstance().getToken();

            if ((token != null && token.trim().length() > 0) && (!token.equals(localToken))) {
                PreferenceUtil.setFcmToken(token);
            }

        }

    }
}
