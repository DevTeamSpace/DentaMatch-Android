package com.appster.dentamatch.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.NetworkMonitor;
import com.appster.dentamatch.util.PlayServicesUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.socialhelper.SocialAuthError;
import com.appster.dentamatch.util.socialhelper.SocialAuthListener;
import com.appster.dentamatch.util.socialhelper.SocialMediaHelper;
import com.appster.dentamatch.util.socialhelper.SocialProfile;
import com.appster.dentamatch.util.socialhelper.SocialType;

import static com.appster.dentamatch.util.Constants.ACTIVITIES.SWITCH_ACTIVITY;

public class SwitchActivity extends BaseActivity implements SocialAuthListener<SocialProfile> {

    Handler handler = new Handler();
    public void forwardToLogin() {
        //startActivityForResult(new Intent(this, LoginActivity.class), Constants.REQUEST_CODE.REQUEST_CODE_FIRST_LOGIN);
    }

    void continueToNextScreen() {
        if (!PreferenceUtil.isJoyRideComplete()) {
            forwardToJoyRide();
        }/*else if (!PreferenceUtil.isLogin() && !PreferenceUtil.isLoginAppeared() && !PreferenceUtil.isLoginSkipped()) {
            forwardToLogin();
        } else {
            forwardToHome();
        }*/
    }

    void continueToNextService() {
        /*if (PreferenceUtils.isLogin()) {
            showProgress();
            RequestController.autoLogin(this, RequestJsonUtil.getLoginSignUpJsonRequest(PreferenceUtils.getUserEmailId(), "", true));
        } else {
            afterLoginSuccess();
        }*/

    }

    void startServiceCalling() {
        if (NetworkMonitor.isNetworkAvailable()) {
            showProgress();
            if (!PreferenceUtil.isFirstTimeLaunch()) {
                //RequestController.postFirstLaunch(this)
                continueToNextScreen();
            }
            else {
                /*int buildVersion = PreferenceUtils.getBuildVersion();
                if (BuildConfig.VERSION_CODE > buildVersion) {
                    PreferenceUtils.removeAllConfig();
                }
                if (PreferenceUtils.getImageConfiguration() != null && PreferenceUtils.getVisualConfiguration() != null && PreferenceUtils.getAppConfiguration() != null) {
                    continueToNextService();
                }
                RequestController.getLayoutConfiguration(this, RequestJsonUtil.configRequestJson());*/
            }
        } else
            showRetryMessage(getString(R.string.error_no_network_connection));
    }

    void afterLoginSuccess() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            forwardToHome(data);
        } else {
            continueToNextScreen();
        }
    }

    public void forwardToJoyRide() {
//        startActivityForResult(new Intent(this, AppIntroActivity.class), Constants.REQUEST_CODE.REQUEST_CODE_JOYRIDE);
    }

    public void forwardToHome(String data) {
        /*Intent intent = new Intent(this, HomeActivity.class);
        if (data != null) {
            intent.putExtra(BundleKey.EXTRA_ACTION_BROWSE, true);
            intent.putExtra(BundleKey.EXTRA_ACTION_BROWSE_DATA, data);
        }
        startActivity(intent);
        finish();*/
    }

    public void forwardToHome() {
        forwardToHome(null);
    }

    private SocialMediaHelper mSocialProvider = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mIsSocialLogin) {
            mSocialProvider.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_CODE_JOYRIDE:
                continueToNextScreen();
                break;
        }
    }

    void showProgress() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    void hideProgress() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public String getActivityName() {
        return SWITCH_ACTIVITY.name();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!BuildConfig.DEBUG && Utils.isSimulator()) {
            return;
        }
        if (!BuildConfig.DEBUG && !PlayServicesUtils.checkGooglePlaySevices(this)) {
            //PlayServicesUtils.handleAnyPlayServicesError(this);
            return;
        }
        startServiceCalling();
    }

    void showRetryMessage(String message) {
        hideProgress();
        showSnackBar(message, "Retry", listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startServiceCalling();
        }
    };


    private void facebookLogin() {
        mIsSocialLogin = true;
        if (mSocialProvider == null)
            mSocialProvider = new SocialMediaHelper(this, this);
        mSocialProvider.setSocialType(SocialType.FACEBOOK);
        mSocialProvider.initProcess();
    }


    @Override
    public void onExecute(SocialType provider, final SocialProfile socialProfile) {
        SocialMediaHelper.logoutGoogleClient();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mIsSocialLogin = true;
//                alive = true;
                if (socialProfile.getEmail() == null || socialProfile.getEmail().isEmpty()) {
                    showSnackBar(getString(R.string.error_unable_reterive_email));
                } else {
                    //processSocialLogin(socialProfile);
                }
            }
        });
    }

    private boolean mIsSocialLogin = false;
    @Override
    public void onError(SocialAuthError e) {
        mIsSocialLogin = false;
        SocialMediaHelper.logoutSession();
        showSnackBar(getString(R.string.error_unable_access_account));
    }


    private void googleLogin() {
        mIsSocialLogin = true;

        if (mSocialProvider == null)
            mSocialProvider = new SocialMediaHelper(this, this);
        mSocialProvider.setSocialType(SocialType.GOOGLE);
        mSocialProvider.initProcess();
    }

}