package com.appster.dentamatch.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySettingsBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.ChangeUserLocation;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.auth.ResetPasswordActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

import static com.appster.dentamatch.util.Constants.REQUEST_CODE.REQUEST_CODE_LOCATION;

/**
 * Created by virender on 17/01/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySettingsBinding settingsBinding;
    private UserModel mUserModel;

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
        mUserModel = PreferenceUtil.getUserModel();
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
                Intent intent = new Intent(this, PlacesMapActivity.class);

                if (mUserModel.getLatitude() != null && mUserModel.getLongitude() != null) {
                    intent.putExtra(Constants.EXTRA_LATITUDE, mUserModel.getLatitude());
                    intent.putExtra(Constants.EXTRA_LONGITUDE, mUserModel.getLongitude());
                    intent.putExtra(Constants.EXTRA_POSTAL_CODE, mUserModel.getPostalCode());
                    intent.putExtra(Constants.EXTRA_PLACE_NAME, mUserModel.getPreferredJobLocation());
                }

                startActivityForResult(intent, REQUEST_CODE_LOCATION);
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

            default: break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_LOCATION){
            if(data != null){
                if (data.hasExtra(Constants.EXTRA_PLACE_NAME)) {
                    String lat = data.getStringExtra(Constants.EXTRA_LATITUDE);
                    String lng = data.getStringExtra(Constants.EXTRA_LONGITUDE);
                    String address = data.getStringExtra(Constants.EXTRA_PLACE_NAME);
                    String zipCode = data.getStringExtra(Constants.EXTRA_POSTAL_CODE);

                    if(TextUtils.isEmpty(zipCode)){
                        showToast(getString(R.string.msg_empty_zip_code));
                    }else {
                        updateUserLocation(lat, lng, zipCode, address);
                    }
                }
            }
        }
    }

    private void updateUserLocation(final String lat, final String lng,final String zipCode, final String address) {
        ChangeUserLocation request = new ChangeUserLocation();
        request.setLatitude(lat);
        request.setLongitude(lng);
        request.setPreferredLocation(address);
        request.setZipCode(Integer.valueOf(zipCode));

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.updateUserLocation(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                showToast(response.getMessage());
                if(response.getStatus() == 1){
                    mUserModel.setLongitude(lng);
                    mUserModel.setLatitude(lat);
                    mUserModel.setPreferredJobLocation(address);
                    mUserModel.setPostalCode(zipCode);
                    PreferenceUtil.setUserModel(mUserModel);
                    EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    private void callLogoutApi() {

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.logout().enqueue(new BaseCallback<BaseResponse>(SettingActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());
                localLogOut();
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });

    }
}
