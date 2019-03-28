/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityForgotPasswordBinding;
import com.appster.dentamatch.util.Utils;

/**
 * Created by virender on 30/12/16.
 * To inject activity reference.
 */
public class ForgotPasswordActivity extends BaseLoadingActivity<ForgotPasswordViewModel>
        implements View.OnClickListener {

    private ActivityForgotPasswordBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarForgotPassword.tvToolbarGeneralLeft.setText(getString(R.string.header_forgot_password));
        mBinder.toolbarForgotPassword.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnSave.setOnClickListener(this);
        viewModel.getForgotPassword().observe(this, (completed) -> {
            if (Boolean.TRUE.equals(completed)) finish();
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivToolBarLeft:
                hideKeyboard();
                onBackPressed();
                break;
            case R.id.btn_save:
                if (validateInput()) {
                    hideKeyboard();
                    forgotPasswordApi();
                }
                break;
            default:
                break;
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(mBinder.etEmail.getText().toString())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mBinder.etEmail.getText().toString()).matches()) {
            Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
            return false;
        }
        return true;
    }

    private void forgotPasswordApi() {
        viewModel.forgotPassword(getTextFromEditText(mBinder.etEmail));
    }
}
