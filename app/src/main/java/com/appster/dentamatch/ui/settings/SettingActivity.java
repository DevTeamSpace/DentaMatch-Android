package com.appster.dentamatch.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySettingsBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.auth.LoginActivity;
import com.appster.dentamatch.ui.auth.ResetPasswordActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import retrofit2.Call;

/**
 * Created by virender on 17/01/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySettingsBinding settingsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initViews();
    }

    private void initViews() {
        settingsBinding.toolbarSetting.tvToolbarGeneralLeft.setText(getString(R.string.header_settings));
        settingsBinding.toolbarSetting.ivToolBarLeft.setOnClickListener(this);
        settingsBinding.tvChnageLocation.setOnClickListener(this);
        settingsBinding.tvResetPassword.setOnClickListener(this);
        settingsBinding.tvLogout.setOnClickListener(this);
        settingsBinding.tvTermNCondition.setOnClickListener(this);
        settingsBinding.tvPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_password:
                startActivity(new Intent(SettingActivity.this, ResetPasswordActivity.class));

                break;
            case R.id.iv_tool_bar_left:
                finish();

                break;
            case R.id.tv_chnage_location:
//                startActivity(new Intent(SettingActivity.this,));
                startActivity(new Intent(this, PlacesMapActivity.class));

                break;
            case R.id.tv_logout:
                Alert.createYesNoAlert(SettingActivity.this, getString(R.string.yes), getString(R.string.no), "", getString(R.string.alert_logout), new Alert.OnAlertClickListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        callLogoutApi();
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.tv_privacy_policy:
                startActivity(new Intent(SettingActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.FROM_WHERE, true));

                break;
            case R.id.tv_term_n_condition:
                startActivity(new Intent(SettingActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.FROM_WHERE, false));

                break;
        }

    }

    private void callLogoutApi() {

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.logout().enqueue(new BaseCallback<BaseResponse>(SettingActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    PreferenceUtil.setIsLogined(false);
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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
