package com.appster.dentamatch.ui.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityForgotPasswordBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import retrofit2.Call;

/**
 * Created by virender on 30/12/16.
 */
public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "ForgotPasswordActivity";

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;

            case R.id.btn_save:
                if (validateInput()) {
                    hideKeyboard();
                    forgotPasswordApi(prepareForgotPasswordRequest());
                }
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

    private LoginRequest prepareForgotPasswordRequest() {
        processToShowDialog("", getString(R.string.please_wait), null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(mBinder.etEmail.getText().toString());
        return loginRequest;
    }

    private void forgotPasswordApi(LoginRequest loginRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.forgotPassword(loginRequest).enqueue(new BaseCallback<LoginResponse>(ForgotPasswordActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());
                if (response.getStatus() == 1) {
                    finish();
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });
    }

    @Override
    public String getActivityName() {
        return null;
    }


}
