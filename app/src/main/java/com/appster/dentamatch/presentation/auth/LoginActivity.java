/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.auth;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityLoginBinding;
import com.appster.dentamatch.domain.auth.AuthResponse;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.presentation.common.HomeActivity;
import com.appster.dentamatch.presentation.profile.CreateProfileActivity;
import com.appster.dentamatch.presentation.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static com.appster.dentamatch.util.Constants.REQUEST_CODE.REQUEST_CODE_LOCATION;

/**
 * Created by virender on 13/12/16.
 * To inject activity reference.
 */
public class LoginActivity extends BaseLoadingActivity<LoginViewModel> implements View.OnClickListener {

    private ActivityLoginBinding mBinder;

    private boolean mIsAccepted;
    private boolean mIsLogin;
    private boolean mIsLoginShow;
    private boolean mIsRegisterShow;
    private int mPreferredJobLocationId;

    private ArrayAdapter<PreferredJobLocationData> mPreferredJobLocationDataArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_IS_LOGIN)) {
            mIsLogin = getIntent().getBooleanExtra(Constants.EXTRA_IS_LOGIN, false);
        }
        initViews();
    }

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
        showSelectedView(mIsLogin);
    }

    private void setPolicySpanString() {
        SpannableString spanString = new SpannableString(getString(R.string.label_accept_term_n_condition));

        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, false);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int tncStart = getString(R.string.label_accept_term_n_condition).indexOf("Term");
        int tncEnd = getString(R.string.label_accept_term_n_condition).lastIndexOf("and") - 1;
        Utils.setSpannClickEvent(spanString, tncStart, tncEnd, termsAndCondition);
        Utils.setSpannColor(spanString, tncStart, tncEnd, ContextCompat.getColor(this, R.color.button_bg_color));

        int privacyStart = getString(R.string.label_accept_term_n_condition).indexOf("Privacy");
        Utils.setSpannClickEvent(spanString, privacyStart + 1, spanString.length(), privacy);
        Utils.setSpannColor(spanString, privacyStart, spanString.length(), ContextCompat.getColor(this, R.color.button_bg_color));
        Utils.setSpannCommanProperty(mBinder.tvTermNPolicy, spanString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_view_login:
                mIsLogin = true;
                hideKeyboard();
                showSelectedView(true);
                break;
            case R.id.login_view_register:
                mIsLogin = false;
                hideKeyboard();
                showSelectedView(false);
                break;
            case R.id.login_tv_show_password:
                if (mBinder.loginEtPassword.getText().toString().length() > 0) {
                    if (mIsLoginShow) {
                        Utils.showPassword(LoginActivity.this,
                                mBinder.loginEtPassword,
                                true,
                                mBinder.loginTvShowPassword);
                        mIsLoginShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this,
                                mBinder.loginEtPassword,
                                false,
                                mBinder.loginTvShowPassword);
                        mIsLoginShow = true;
                    }
                }
                break;
            case R.id.register_tv_show_password:
                if (mBinder.registerEtPassword.getText().toString().length() > 0) {
                    if (mIsRegisterShow) {
                        Utils.showPassword(LoginActivity.this,
                                mBinder.registerEtPassword,
                                true,
                                mBinder.registerTvShowPassword);
                        mIsRegisterShow = false;
                    } else {
                        Utils.showPassword(LoginActivity.this,
                                mBinder.registerEtPassword,
                                false,
                                mBinder.registerTvShowPassword);
                        mIsRegisterShow = true;
                    }
                }
                break;
            case R.id.login_btn_register:
                hideKeyboard();
                if (validateInput()) {
                    if (mIsLogin) {
                        signInApi();
                    } else {
                        signUpApi();
                    }
                }
                break;
            case R.id.login_tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.iv_accept_policy:
                if (mIsAccepted) {
                    mIsAccepted = false;
                    mBinder.ivAcceptPolicy.setImageResource(R.drawable.ic_check_empty);
                } else {
                    mBinder.ivAcceptPolicy.setImageResource(R.drawable.ic_check_fill);
                    mIsAccepted = true;
                }
                break;
            case R.id.tv_preferred_job_location:
                if (mPreferredJobLocationDataArrayAdapter == null) {
                    viewModel.getPreferredJobLocationList();
                    viewModel.getPreferredLocationsList().observe(this, mLocationsListObserver);
                } else {
                    showLocationList();
                }
                break;
        }
    }

    private final Observer<PreferredJobLocationModel> mLocationsListObserver = this::onLocationChanged;

    private void showLocationList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoginActivity.this);
        builderSingle.setTitle(getResources().getString(R.string.preferred_location_label));
        builderSingle.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(mPreferredJobLocationDataArrayAdapter, (dialog, which) -> {
            PreferredJobLocationData locationData = mPreferredJobLocationDataArrayAdapter.getItem(which);
            if (locationData != null) {
                mBinder.tvPreferredJobLocation.setText(locationData.getPreferredLocationName());
                mPreferredJobLocationId = locationData.getId();
            }
            mBinder.tvPreferredJobLocation.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_color));
        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        String mPlaceName = bundle.getString(Constants.EXTRA_PLACE_NAME);
                        mBinder.tvPreferredJobLocation.setText(mPlaceName);
                    }
                }
        }
    }

    private boolean validateInput() {
        if (mIsLogin) {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtEmail))) {
                showSnackBar(getString(R.string.blank_email_alert));
                mBinder.loginEtEmail.requestFocus();
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.loginEtEmail)).matches()) {
                mBinder.loginEtEmail.requestFocus();
                showSnackBar(getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtPassword))) {
                mBinder.loginEtPassword.requestFocus();
                showSnackBar(getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).contains(" ")) {
                mBinder.loginEtPassword.requestFocus();
                showSnackBar(getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.loginEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.loginEtPassword.requestFocus();
                showSnackBar(getString(R.string.password_min_length_alert));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtFname))) {
                mBinder.registerEtFname.requestFocus();
                showSnackBar(getString(R.string.blank_fname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtLname))) {
                mBinder.registerEtLname.requestFocus();
                showSnackBar(getString(R.string.blank_lname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtEmail))) {
                mBinder.registerEtEmail.requestFocus();
                showSnackBar(getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.registerEtEmail)).matches()) {
                mBinder.registerEtEmail.requestFocus();
                showSnackBar(getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtPassword))) {
                mBinder.registerEtPassword.requestFocus();
                showSnackBar(getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).contains(" ")) {
                mBinder.registerEtPassword.requestFocus();
                showSnackBar(getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.registerEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.registerEtPassword.requestFocus();
                showSnackBar(getString(R.string.password_min_length_alert));
                return false;
            }
            if (TextUtils.isEmpty(mBinder.tvPreferredJobLocation.getText().toString())) {
                mBinder.tvPreferredJobLocation.requestFocus();
                showSnackBar(getString(R.string.blank_location_alert));
                return false;
            }
            if (!mIsAccepted) {
                showSnackBar(getString(R.string.blank_tnc_alert));
                return false;
            }
        }
        return true;
    }

    private final Observer<AuthResponse> mSignUpObserver = this::onSignUpChanged;

    private void signUpApi() {
        viewModel.signUp(getTextFromEditText(mBinder.registerEtEmail),
                getTextFromEditText(mBinder.registerEtPassword),
                getTextFromEditText(mBinder.registerEtFname),
                getTextFromEditText(mBinder.registerEtLname),
                mPreferredJobLocationId);
        viewModel.getLogin().observe(this, mSignUpObserver);
    }

    private Observer<AuthResponse> mLoginObserver = this::onLoginChanged;

    private void signInApi() {
        viewModel.signIn(getTextFromEditText(mBinder.loginEtEmail), getTextFromEditText(mBinder.loginEtPassword));
        viewModel.getLogin().observe(this, mLoginObserver);
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

    private void showSignUpSuccessDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.success))
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    PreferenceUtil.setIsLogin(true);
                    PreferenceUtil.setProfileCompleted(false);
                    Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
                    startActivity(intent);
                    finish();
                }).setCancelable(false)
                .show();
    }

    private void onLocationChanged(@Nullable PreferredJobLocationModel preferredJobLocationModel) {
        viewModel.getPreferredLocationsList().removeObserver(mLocationsListObserver);
        hideKeyboard();
        if (preferredJobLocationModel != null) {
            ArrayList<PreferredJobLocationData> mPreferredJobLocationList = preferredJobLocationModel
                    .getResult()
                    .getPreferredJobLocations();
            mPreferredJobLocationDataArrayAdapter =
                    new PreferredJobLocationAdapter(LoginActivity.this,
                            mPreferredJobLocationList);
            showLocationList();
        }
    }

    private void onSignUpChanged(@Nullable AuthResponse response) {
        viewModel.getLogin().removeObserver(mSignUpObserver);
        if (response != null && response.getStatus() == 1) {
            mIsLogin = true;
            showSignUpSuccessDialog(response.getMessage());
        }
    }

    private void onLoginChanged(@Nullable AuthResponse response) {
        viewModel.getLogin().removeObserver(mLoginObserver);
        if (response != null && response.getStatus() == 1) {
            if (response.getLoginResponseData().getUserDetail().getJobTitileId() == null) {
                PreferenceUtil.setProfileCompleted(false);
                Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                PreferenceUtil.setProfileCompleted(true);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
}

