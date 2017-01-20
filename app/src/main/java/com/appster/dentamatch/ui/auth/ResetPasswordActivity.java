package com.appster.dentamatch.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityResetPasswordBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.ChangePassowrdRequest;
import com.appster.dentamatch.network.response.affiliation.AffiliationPostRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.CertificateActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import retrofit2.Call;

/**
 * Created by virender on 30/12/16.
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityResetPasswordBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        mBinder.btnSave.setOnClickListener(this);

    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                break;
        }
    }

    private void callResetPassword(ChangePassowrdRequest request) {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.changePassword(request).enqueue(new BaseCallback<BaseResponse>(ResetPasswordActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }
}
