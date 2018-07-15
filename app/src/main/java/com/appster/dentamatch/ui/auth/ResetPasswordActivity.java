/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityResetPasswordBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.ChangePasswordRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import retrofit2.Call;

/**
 * Created by virender on 30/12/16.
 * To inject activity reference.
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityResetPasswordBinding mBinder;
    private boolean isOldSHow, isNewShow, isConfirmShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        mBinder.toolbarResetPassword.tvToolbarGeneralLeft.setText(getString(R.string.settings_reset_password).toUpperCase());
        mBinder.btnSave.setOnClickListener(this);
        mBinder.tvShowConfirmPassword.setOnClickListener(this);
        mBinder.tvShowNewPassword.setOnClickListener(this);
        mBinder.tvShowOldPassword.setOnClickListener(this);
        mBinder.toolbarResetPassword.ivToolBarLeft.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (checkValidation()) {
                    callResetPassword(prepareRequest());
                }
                break;
            case R.id.tv_show_confirm_password:
                if (mBinder.etConfirmPassword.getText().toString().length() > 0) {

                    if (isConfirmShow) {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etConfirmPassword,
                                isConfirmShow,
                                mBinder.tvShowConfirmPassword);
                        isConfirmShow = false;

                    } else {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etConfirmPassword,
                                isConfirmShow,
                                mBinder.tvShowConfirmPassword);
                        isConfirmShow = true;
                    }
                }
                break;

            case R.id.tv_show_old_password:
                if (mBinder.etOldPassword.getText().toString().length() > 0) {

                    if (isOldSHow) {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etOldPassword,
                                isOldSHow,
                                mBinder.tvShowOldPassword);
                        isOldSHow = false;

                    } else {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etOldPassword,
                                isOldSHow,
                                mBinder.tvShowOldPassword);
                        isOldSHow = true;
                    }
                }
                break;
            case R.id.tv_show_new_password:
                if (mBinder.etNewPassword.getText().toString().length() > 0) {

                    if (isNewShow) {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etNewPassword,
                                isNewShow,
                                mBinder.tvShowNewPassword);
                        isNewShow = false;

                    } else {
                        Utils.showPassword(ResetPasswordActivity.this,
                                mBinder.etNewPassword,
                                isNewShow,
                                mBinder.tvShowNewPassword);
                        isNewShow = true;
                    }
                }
                break;
            case R.id.iv_tool_bar_left:
                finish();
                break;

        }
    }

    private boolean checkValidation() {

        if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.etOldPassword))) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_alert_old_password));
            return false;
        }
        if (Utils.getStringFromEditText(mBinder.etOldPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                Utils.getStringFromEditText(mBinder.etOldPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
            return false;
        }
        if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.etNewPassword))) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_alert_new_password));
            return false;
        }
        if (Utils.getStringFromEditText(mBinder.etNewPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                Utils.getStringFromEditText(mBinder.etNewPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
            return false;
        }
        if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.etConfirmPassword))) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_alert_confirm_password));
            return false;
        }
        if (Utils.getStringFromEditText(mBinder.etConfirmPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                Utils.getStringFromEditText(mBinder.etConfirmPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
            return false;
        }


        if (!Utils.getStringFromEditText(mBinder.etConfirmPassword).equalsIgnoreCase(Utils.getStringFromEditText(mBinder.etNewPassword))) {
            Utils.showToast(getApplicationContext(), getString(R.string.alert_reset_password_mismatch));
            return false;
        }
        return true;

    }

    private ChangePasswordRequest prepareRequest() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmNewPassword(mBinder.etConfirmPassword.getText().toString());
        changePasswordRequest.setOldPassword(mBinder.etOldPassword.getText().toString());
        changePasswordRequest.setNewPassword(mBinder.etNewPassword.getText().toString());
        return changePasswordRequest;
    }

    private void callResetPassword(ChangePasswordRequest request) {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.changePassword(request).enqueue(new BaseCallback<BaseResponse>(ResetPasswordActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }

}
