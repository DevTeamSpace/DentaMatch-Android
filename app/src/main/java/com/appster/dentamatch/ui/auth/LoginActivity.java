package com.appster.dentamatch.ui.auth;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

import retrofit2.Call;

/**
 * Created by virender on 13/12/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private static final int REQUEST_CODE_LOCATION = 101;
    private ImageView ivRegisterPeg, ivLoginPeg, ivPolicy;
    private LinearLayout layoutRegisterSelector, layoutLoginSelector, layoutOnlyRegister, layoutOnlyLogin;
    private TextView tvLogin, tvRegister, tvForgotPassword, tvTermNcondition, tvLoginShowPassword, tvRegisterShowPassword;
    private EditText etRegisterFName, etRegisterLName, etRegisterPassword, etRegisterEmail, etLoginEmail, etLoginPassword;
    private Button btnRegister;
    private CustomTextView tvPreferredJobLocation;
    private boolean isAccepted, isLogin;

    private String mPostalCode;
    private String mPlaceName;
    private String mLatitude;
    private String mLongitude;
    private boolean isLoginShow, isRegisterShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {

        tvRegister = (TextView) findViewById(R.id.register_tv_register);
        tvLogin = (TextView) findViewById(R.id.login_tv_login);
        tvTermNcondition = (TextView) findViewById(R.id.tv_term_n_policy);
        tvLoginShowPassword = (TextView) findViewById(R.id.login_tv_show_password);
        tvRegisterShowPassword = (TextView) findViewById(R.id.register_tv_show_password);
        tvForgotPassword = (TextView) findViewById(R.id.login_tv_forgot_password);

        etLoginEmail = (EditText) findViewById(R.id.login_et_email);
        etLoginPassword = (EditText) findViewById(R.id.login_et_password);
        etRegisterEmail = (EditText) findViewById(R.id.register_et_email);
        etRegisterFName = (EditText) findViewById(R.id.register_et_fname);
        etRegisterLName = (EditText) findViewById(R.id.register_et_lname);
        etRegisterPassword = (EditText) findViewById(R.id.register_et_password);

        btnRegister = (Button) findViewById(R.id.login_btn_register);
        ivRegisterPeg = (ImageView) findViewById(R.id.register_iv_peg);
        ivLoginPeg = (ImageView) findViewById(R.id.login_iv_peg);
        ivPolicy = (ImageView) findViewById(R.id.iv_accept_policy);
        layoutRegisterSelector = (LinearLayout) findViewById(R.id.login_view_register);
        layoutLoginSelector = (LinearLayout) findViewById(R.id.login_view_login);
        layoutOnlyLogin = (LinearLayout) findViewById(R.id.login_layout_login_view);
        layoutOnlyRegister = (LinearLayout) findViewById(R.id.login_layout_register_view);
        tvPreferredJobLocation = (CustomTextView) findViewById(R.id.tv_preferred_job_location);

        layoutLoginSelector.setOnClickListener(this);
        layoutRegisterSelector.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvPreferredJobLocation.setOnClickListener(this);
        tvLoginShowPassword.setOnClickListener(this);
        tvRegisterShowPassword.setOnClickListener(this);
        ivPolicy.setOnClickListener(this);
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
        Utils.setSpannColor(spanString, tncStart, tncEnd, getResources().getColor(R.color.button_bg_color));
//        Utils.setSpannTypeface(spanString, tncStart, tncEnd, Typeface.BOLD);
        Utils.setSpannUnderline(spanString, tncStart, tncEnd);

        int privacyStart = getString(R.string.label_accept_term_ncondition).indexOf("Privacy");
        Utils.setSpannClickEvent(spanString, privacyStart + 1, spanString.length(), privacy);
        Utils.setSpannColor(spanString, privacyStart, spanString.length(), getResources().getColor(R.color.button_bg_color));
        Utils.setSpannUnderline(spanString, privacyStart + 1, spanString.length());
//        Utils.setSpannTypeface(spanString, privacyStart, spanString.length(), Typeface.BOLD);

        Utils.setSpannCommanProperty(tvTermNcondition, spanString);
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
                if (etLoginPassword.getText().toString().length() > 0) {

                    if (isLoginShow) {
                        Utils.showPassword(LoginActivity.this, etLoginPassword, isLoginShow, tvLoginShowPassword);
                        isLoginShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, etLoginPassword, isLoginShow, tvLoginShowPassword);
                        isLoginShow = true;
                    }
                }
                break;
            case R.id.register_tv_show_password:
                if (etRegisterPassword.getText().toString().length() > 0) {

                    if (isRegisterShow) {
                        Utils.showPassword(LoginActivity.this, etRegisterPassword, isRegisterShow, tvRegisterShowPassword);
                        isRegisterShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, etRegisterPassword, isRegisterShow, tvRegisterShowPassword);
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
                    ivPolicy.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    ivPolicy.setBackgroundResource(R.drawable.ic_check_fill);
                    isAccepted = true;
                }
                break;

            case R.id.tv_preferred_job_location:
                startActivityForResult(new Intent(this, PlacesMapActivity.class), REQUEST_CODE_LOCATION);
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
                        tvPreferredJobLocation.setText(data.getExtras().getString(Constants.EXTRA_PLACE_NAME));
                    }
                }
        }
    }

    private boolean validateInput() {
        if (isLogin) {
            if (TextUtils.isEmpty(getTextFromEditText(etLoginEmail))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(etLoginEmail)).matches()) {
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(etLoginPassword))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(etLoginPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(etLoginPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(getTextFromEditText(etRegisterFName))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_fname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(etRegisterLName))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_lname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(etRegisterEmail))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(etRegisterEmail)).matches()) {
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(etRegisterPassword))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(etRegisterPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(etRegisterPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
            if (TextUtils.isEmpty(tvPreferredJobLocation.getText().toString())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_location_alert));
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
        loginRequest.setEmail(getTextFromEditText(etRegisterEmail));
        loginRequest.setPassword(getTextFromEditText(etRegisterPassword));
        loginRequest.setFirstName(getTextFromEditText(etRegisterFName));
        loginRequest.setLastName(getTextFromEditText(etRegisterLName));

        loginRequest.setLatitude(mLatitude);
        loginRequest.setLongitude(mLongitude);
        loginRequest.setZipCode(mPostalCode);
        loginRequest.setPreferedLocation(mPlaceName);
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
                    PreferenceUtil.setFistName(getTextFromEditText(etRegisterFName));
                    PreferenceUtil.setLastName(getTextFromEditText(etRegisterLName));
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    isLogin = true;
                    showSelectedView(true);

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
        loginRequest.setEmail(getTextFromEditText(etLoginEmail));
        loginRequest.setPassword(getTextFromEditText(etLoginPassword));
        return loginRequest;
    }

    private String getTextFromEditText(EditText et) {
        return et.getText().toString();
    }

    private void signInApi(LoginRequest loginRequest) {
        LogUtils.LOGD(TAG, "signInApi");

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.signIn(loginRequest).enqueue(new BaseCallback<LoginResponse>(LoginActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    PreferenceUtil.setIsLogined(true);
                    PreferenceUtil.setUserToken(response.getLoginResponseData().getUserDetail().getUserToken());
                    PreferenceUtil.setFistName(response.getLoginResponseData().getUserDetail().getFirstName());
                    PreferenceUtil.setLastName(response.getLoginResponseData().getUserDetail().getLastName());
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });
    }

    private void showSelectedView(boolean isLogin) {
        if (isLogin) {
            layoutOnlyRegister.setVisibility(View.GONE);
            ivRegisterPeg.setVisibility(View.INVISIBLE);
            ivLoginPeg.setVisibility(View.VISIBLE);
            layoutOnlyLogin.setVisibility(View.VISIBLE);
            btnRegister.setText(getString(R.string.login_label));
            Utils.setFontFaceRobotoBold(tvLogin);
            Utils.setFontFaceRobotoLight(tvRegister);

        } else {
            layoutOnlyRegister.setVisibility(View.VISIBLE);
            ivRegisterPeg.setVisibility(View.VISIBLE);
            ivLoginPeg.setVisibility(View.INVISIBLE);
            layoutOnlyLogin.setVisibility(View.GONE);
            btnRegister.setText(getString(R.string.register_label));
            Utils.setFontFaceRobotoBold(tvRegister);
            Utils.setFontFaceRobotoLight(tvLogin);
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }
}
