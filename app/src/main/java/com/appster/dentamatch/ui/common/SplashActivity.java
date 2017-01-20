package com.appster.dentamatch.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.auth.LoginActivity;
import com.appster.dentamatch.ui.onboardtutorial.OnBoardingActivity;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;
import com.appster.dentamatch.util.PreferenceUtil;

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
//        setContentView(R.layout.activity_main);
         /* Service starts to get FCM token if not saved in preference
        * */
//        if(TextUtils.isEmpty(AppPreferences.getInstance().getStringPreference(Constants.FCM_TOKEN))) {
//            startService(new Intent(SplashActivity.this, FCMRegistrationService.class));
//        }

        handler.postDelayed(SplashActivity.this, SPLASH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {

//        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        if (PreferenceUtil.getIsOnBoarding()) {
            if (PreferenceUtil.getIsLogined()) {
//                startActivity(new Intent(SplashActivity.this, CreateProfileActivity1.class));
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));

            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }
        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));

        }
        finish();
    }
}
