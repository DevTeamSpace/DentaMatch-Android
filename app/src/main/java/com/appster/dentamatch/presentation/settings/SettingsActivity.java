/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.settings;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivitySettingsBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.presentation.auth.ResetPasswordActivity;
import com.appster.dentamatch.presentation.map.PlacesMapActivity;
import com.appster.dentamatch.presentation.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import static com.appster.dentamatch.util.Constants.REQUEST_CODE.REQUEST_CODE_LOCATION;

/**
 * Created by virender on 17/01/17.
 * Activity to show the user and application required setting options.
 */
public class SettingsActivity extends BaseLoadingActivity<SettingsViewModel> implements View.OnClickListener {

    private ActivitySettingsBinding settingsBinding;
    private UserModel mUserModel;
    private Observer<Boolean> mLogoutObserver = this::onLogOut;
    private Observer<UserModel> mUserModelObserver = this::onUserModelChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initViews();
    }

    private void initViews() {
        settingsBinding.toolbarSetting.tvToolbarGeneralLeft.setText(getString(R.string.header_settings));
        settingsBinding.toolbarSetting.ivToolBarLeft.setOnClickListener(this);
        settingsBinding.tvChangeLocation.setOnClickListener(this);
        settingsBinding.tvResetPassword.setOnClickListener(this);
        settingsBinding.tvLogout.setOnClickListener(this);
        settingsBinding.tvTermNCondition.setOnClickListener(this);
        settingsBinding.tvPrivacyPolicy.setOnClickListener(this);
        mUserModel = PreferenceUtil.getUserModel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_password:
                startActivity(new Intent(SettingsActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.iv_tool_bar_left:
                finish();
                break;
            case R.id.tv_change_location:
                Intent intent = new Intent(this, PlacesMapActivity.class);
                if (mUserModel.getLatitude() != null && mUserModel.getLongitude() != null) {
                    intent.putExtra(Constants.EXTRA_LATITUDE, mUserModel.getLatitude());
                    intent.putExtra(Constants.EXTRA_LONGITUDE, mUserModel.getLongitude());
                    intent.putExtra(Constants.EXTRA_POSTAL_CODE, mUserModel.getPostalCode());
                    intent.putExtra(Constants.EXTRA_PLACE_NAME, mUserModel.getPreferredJobLocation());
                    intent.putExtra(Constants.EXTRA_COUNTRY_NAME, mUserModel.getPreferredCountry());
                    intent.putExtra(Constants.EXTRA_CITY_NAME, mUserModel.getPreferredCity());
                    intent.putExtra(Constants.EXTRA_STATE_NAME, mUserModel.getPreferredState());
                }
                startActivityForResult(intent, REQUEST_CODE_LOCATION);
                break;
            case R.id.tv_logout:
                Alert.createYesNoAlert(SettingsActivity.this,
                        getString(R.string.yes),
                        getString(R.string.no),
                        "",
                        getString(R.string.alert_logout),
                        new Alert.OnAlertClickListener() {
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
                startActivity(new Intent(SettingsActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.FROM_WHERE, true));
                break;
            case R.id.tv_term_n_condition:
                startActivity(new Intent(SettingsActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.FROM_WHERE, false));
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (data != null) {
                if (data.hasExtra(Constants.EXTRA_PLACE_NAME)) {
                    String lat = data.getStringExtra(Constants.EXTRA_LATITUDE);
                    String lng = data.getStringExtra(Constants.EXTRA_LONGITUDE);
                    String address = data.getStringExtra(Constants.EXTRA_PLACE_NAME);
                    String zipCode = data.getStringExtra(Constants.EXTRA_POSTAL_CODE);
                    String city = data.getStringExtra(Constants.EXTRA_CITY_NAME);
                    String state = data.getStringExtra(Constants.EXTRA_STATE_NAME);
                    String country = data.getStringExtra(Constants.EXTRA_COUNTRY_NAME);

                    if (TextUtils.isEmpty(zipCode)) {
                        showToast(getString(R.string.msg_empty_zip_code));
                    } else {
                        updateUserLocation(lat, lng, zipCode, address, country, state, city);
                    }
                }
            }
        }
    }

    private void updateUserLocation(@Nullable String lat,
                                    @Nullable String lng,
                                    @NonNull String zipCode,
                                    @Nullable String address,
                                    @Nullable String country,
                                    @Nullable String state,
                                    @Nullable String city) {
        viewModel.updateLocation(lat, lng, zipCode, address, country, state, city);
        viewModel.getUserModel().observe(this, mUserModelObserver);
    }

    private void callLogoutApi() {
        viewModel.logout();
        viewModel.getLogout().observe(this, mLogoutObserver);
    }

    private void onUserModelChanged(@Nullable UserModel userModel) {
        if (userModel != null) {
//            showToast(response.getMessage());
            PreferenceUtil.setUserModel(userModel);
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
        }
    }

    private void onLogOut(Boolean logOut) {
        viewModel.getLogout().removeObserver(mLogoutObserver);
        if (Boolean.TRUE.equals(logOut)) {
            localLogOut();
        }
    }
}
