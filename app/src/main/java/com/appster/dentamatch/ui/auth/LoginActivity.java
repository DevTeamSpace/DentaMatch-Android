package com.appster.dentamatch.ui.auth;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityLoginBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.response.auth.SearchFilterModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;

import retrofit2.Call;

import static com.appster.dentamatch.util.Constants.REQUEST_CODE.REQUEST_CODE_LOCATION;

/**
 * Created by virender on 13/12/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private ActivityLoginBinding mBinder;
    private boolean isAccepted, isLogin;

    private String mPostalCode;
    private String mPlaceName;
    private String mLatitude;
    private String mLongitude;
    private boolean isLoginShow, isRegisterShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_login);

        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_IS_LOGIN)) {
            isLogin = getIntent().getBooleanExtra(Constants.EXTRA_IS_LOGIN, false);
        }

        initViews();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {
        mBinder.loginViewLogin.setOnClickListener(this);
        mBinder.loginViewRegister.setOnClickListener(this);
        mBinder.loginTvForgotPassword.setOnClickListener(this);
        mBinder.loginBtnRegister.setOnClickListener(this);
        mBinder.tvPreferredJobLocation.setOnClickListener(this);
        mBinder.loginTvShowPassword.setOnClickListener(this);
        mBinder.registerTvShowPassword.setOnClickListener(this);
        mBinder.ivAcceptPolicy.setOnClickListener(this);

        setPolicySpanString();
        showSelectedView(isLogin);
    }

    private void setPolicySpanString() {
        SpannableString spanString = new SpannableString(
                getString(R.string.label_accept_term_ncondition));

        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, false);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int tncStart = getString(R.string.label_accept_term_ncondition).indexOf("Term");
        int tncEnd = getString(R.string.label_accept_term_ncondition).lastIndexOf("and") - 1;
        Utils.setSpannClickEvent(spanString, tncStart, tncEnd, termsAndCondition);
        Utils.setSpannColor(spanString, tncStart, tncEnd, ContextCompat.getColor(this, R.color.button_bg_color));
        Utils.setSpannUnderline(spanString, tncStart, tncEnd);

        int privacyStart = getString(R.string.label_accept_term_ncondition).indexOf("Privacy");
        Utils.setSpannClickEvent(spanString, privacyStart + 1, spanString.length(), privacy);
        Utils.setSpannColor(spanString, privacyStart, spanString.length(), ContextCompat.getColor(this, R.color.button_bg_color));
        Utils.setSpannUnderline(spanString, privacyStart + 1, spanString.length());
        Utils.setSpannCommanProperty(mBinder.tvTermNPolicy, spanString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_view_login:
                isLogin = true;
                hideKeyboard();
                showSelectedView(true);
                break;
            case R.id.login_view_register:
                isLogin = false;
                hideKeyboard();

                showSelectedView(false);
                break;
            case R.id.login_tv_show_password:
                if (mBinder.loginEtPassword.getText().toString().length() > 0) {

                    if (isLoginShow) {
                        Utils.showPassword(LoginActivity.this, mBinder.loginEtPassword, isLoginShow, mBinder.loginTvShowPassword);
                        isLoginShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, mBinder.loginEtPassword, isLoginShow, mBinder.loginTvShowPassword);
                        isLoginShow = true;
                    }
                }
                break;
            case R.id.register_tv_show_password:
                if (mBinder.registerEtPassword.getText().toString().length() > 0) {

                    if (isRegisterShow) {
                        Utils.showPassword(LoginActivity.this, mBinder.registerEtPassword, isRegisterShow, mBinder.registerTvShowPassword);
                        isRegisterShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, mBinder.registerEtPassword, isRegisterShow, mBinder.registerTvShowPassword);
                        isRegisterShow = true;
                    }
                }
                break;

            case R.id.login_btn_register:
                hideKeyboard();

                if (validateInput()) {

                    if (isLogin) {
                        signInApi(prepareLoginRequest());
                    } else {
                        signUpApi(prepareSignUpRequest());
                    }
                }
                break;

            case R.id.login_tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;

            case R.id.iv_accept_policy:
                if (isAccepted) {
                    isAccepted = false;
                    mBinder.ivAcceptPolicy.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    mBinder.ivAcceptPolicy.setBackgroundResource(R.drawable.ic_check_fill);
                    isAccepted = true;
                }
                break;

            case R.id.tv_preferred_job_location:
                Intent intent = new Intent(this, PlacesMapActivity.class);

                if (mLatitude != null && mLongitude != null) {
                    intent.putExtra(Constants.EXTRA_LATITUDE, mLatitude);
                    intent.putExtra(Constants.EXTRA_LONGITUDE, mLongitude);
                    intent.putExtra(Constants.EXTRA_POSTAL_CODE, mPostalCode);
                    intent.putExtra(Constants.EXTRA_PLACE_NAME, mPlaceName);
                }

                startActivityForResult(intent, REQUEST_CODE_LOCATION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        mLatitude = bundle.getString(Constants.EXTRA_LATITUDE);
                        mLongitude = bundle.getString(Constants.EXTRA_LONGITUDE);
                        mPlaceName = bundle.getString(Constants.EXTRA_PLACE_NAME);
                        mPostalCode = bundle.getString(Constants.EXTRA_POSTAL_CODE);
                        mBinder.tvPreferredJobLocation.setText(mPlaceName);
                    }
                }
        }
    }

    private boolean validateInput() {
        if (isLogin) {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtEmail))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                mBinder.loginEtEmail.requestFocus();
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.loginEtEmail)).matches()) {
                mBinder.loginEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtPassword))) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).contains(" ")) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.loginEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtFname))) {
                mBinder.registerEtFname.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_fname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtLname))) {
                mBinder.registerEtLname.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_lname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtEmail))) {
                mBinder.registerEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.registerEtEmail)).matches()) {
                mBinder.registerEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtPassword))) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).contains(" ")) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.registerEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
            if (TextUtils.isEmpty(mBinder.tvPreferredJobLocation.getText().toString())) {
                mBinder.tvPreferredJobLocation.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_location_alert));
                return false;
            }
            if (mPostalCode.isEmpty()) {
                Utils.showToastLong(getApplicationContext(), getString(R.string.blank_postal_code));
                return false;
            }
            if (!isAccepted) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_tnc_alert));
                return false;
            }
        }
        return true;
    }

    private LoginRequest prepareSignUpRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(Utils.getDeviceID(getApplicationContext()));
        loginRequest.setDeviceToken(Utils.getDeviceToken());
        loginRequest.setDeviceType(Constants.DEVICE_TYPE);
        loginRequest.setEmail(getTextFromEditText(mBinder.registerEtEmail));
        loginRequest.setPassword(getTextFromEditText(mBinder.registerEtPassword));
        loginRequest.setFirstName(getTextFromEditText(mBinder.registerEtFname));
        loginRequest.setLastName(getTextFromEditText(mBinder.registerEtLname));

        loginRequest.setLatitude(mLatitude);
        loginRequest.setLongitude(mLongitude);
        loginRequest.setZipCode(mPostalCode);
        loginRequest.setPreferredLocation(mPlaceName);
        return loginRequest;
    }

    private void signUpApi(LoginRequest loginRequest) {
        LogUtils.LOGD(TAG, "signUpApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.signUp(loginRequest).enqueue(new BaseCallback<LoginResponse>(LoginActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");

                if (response.getStatus() == 1) {
                    PreferenceUtil.setFistName(getTextFromEditText(mBinder.registerEtFname));
                    PreferenceUtil.setLastName(getTextFromEditText(mBinder.registerEtLname));
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    isLogin = true;
                    showSelectedView(true);
                    clearRegistrationFields();

//                    Intent intent = new Intent(getApplicationContext(), CreateProfileActivity1.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");

            }
        });
    }

    private LoginRequest prepareLoginRequest() {
        processToShowDialog("", getString(R.string.please_wait), null);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(Utils.getDeviceID(getApplicationContext()));
        loginRequest.setDeviceToken(Utils.getDeviceToken());
        loginRequest.setDeviceType(Constants.DEVICE_TYPE);
        loginRequest.setEmail(getTextFromEditText(mBinder.loginEtEmail));
        loginRequest.setPassword(getTextFromEditText(mBinder.loginEtPassword));
        return loginRequest;
    }

    private String getTextFromEditText(EditText et) {
        return et.getText().toString().trim();
    }

    private void signInApi(LoginRequest loginRequest) {
        LogUtils.LOGD(TAG, "signInApi");

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.signIn(loginRequest).enqueue(new BaseCallback<LoginResponse>(LoginActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {

                    if (response.getLoginResponseData().getSearchFilters() != null) {
                        SearchFilterModel searchFilters = response.getLoginResponseData().getSearchFilters();
                        SearchJobRequest request = new SearchJobRequest();
                        request.setIsParttime(searchFilters.getIsParttime());
                        request.setIsFulltime(searchFilters.getIsFulltime());

                        request.setLat(searchFilters.getLat());
                        request.setLng(searchFilters.getLng());
                        request.setJobTitle(searchFilters.getJobTitle());
                        request.setPage(1);

                        if(searchFilters.getParttimeDays() != null && searchFilters.getParttimeDays().size() >0) {
                            request.setParttimeDays(searchFilters.getParttimeDays());
                        }else{
                            request.setParttimeDays(new ArrayList<String>());
                        }

                        request.setZipCode(searchFilters.getZipCode());

                        /**
                         * This value is set in order to redirect user from login or splash screen.
                         */
                        PreferenceUtil.setJobFilter(true);
                        PreferenceUtil.saveJobFilter(request);
                    }

                    PreferenceUtil.setIsLogined(true);
                    PreferenceUtil.setUserToken(response.getLoginResponseData().getUserDetail().getUserToken());
                    PreferenceUtil.setFistName(response.getLoginResponseData().getUserDetail().getFirstName());
                    PreferenceUtil.setLastName(response.getLoginResponseData().getUserDetail().getLastName());
                    PreferenceUtil.setProfileImagePath(response.getLoginResponseData().getUserDetail().getImageUrl());
                    PreferenceUtil.setUserChatId(response.getLoginResponseData().getUserDetail().getId());
                    Intent intent = new Intent(getApplicationContext(), CreateProfileActivity1.class);
                    startActivity(intent);
                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
//                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });
    }

    private void showSelectedView(boolean isLogin) {
        if (isLogin) {
            mBinder.loginLayoutRegisterView.setVisibility(View.GONE);
            mBinder.registerIvPeg.setVisibility(View.INVISIBLE);
            mBinder.ivPeg.setVisibility(View.VISIBLE);
            mBinder.loginLayoutLoginView.setVisibility(View.VISIBLE);
            mBinder.loginBtnRegister.setText(getString(R.string.login_label));
            Utils.setFontFaceRobotoBold(mBinder.tvLogin);
            Utils.setFontFaceRobotoLight(mBinder.registerTvRegister);

        } else {
            mBinder.loginLayoutRegisterView.setVisibility(View.VISIBLE);
            mBinder.registerIvPeg.setVisibility(View.VISIBLE);
            mBinder.ivPeg.setVisibility(View.INVISIBLE);
            mBinder.loginLayoutLoginView.setVisibility(View.GONE);
            mBinder.loginBtnRegister.setText(getString(R.string.register_label));
            Utils.setFontFaceRobotoBold(mBinder.registerTvRegister);
            Utils.setFontFaceRobotoLight(mBinder.tvLogin);
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void clearRegistrationFields() {
        mBinder.registerEtFname.getText().clear();
        mBinder.registerEtLname.getText().clear();
        mBinder.registerEtEmail.getText().clear();
        mBinder.registerEtPassword.getText().clear();
        mBinder.tvPreferredJobLocation.setText("");
        mBinder.ivAcceptPolicy.setBackgroundResource(R.drawable.ic_check_empty);
    }

    private void clearLoginFields() {
        mBinder.loginEtEmail.getText().clear();
        mBinder.loginEtPassword.getText().clear();
    }
}
