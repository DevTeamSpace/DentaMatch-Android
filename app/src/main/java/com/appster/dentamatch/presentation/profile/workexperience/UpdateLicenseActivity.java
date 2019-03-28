/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityUpdateLicenseBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.network.request.auth.LicenseRequest;
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by virender on 20/01/17.
 * To inject activity reference.
 */
public class UpdateLicenseActivity extends BaseLoadingActivity<UpdateLicenseViewModel>
        implements View.OnClickListener {

    private ActivityUpdateLicenseBinding mBinder;
    private LicenseRequest data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_update_license);
        initView();
        viewModel.getLicenseUpdate().observe(this, this::onSuccessLicenseUpdate);
    }

    private void onSuccessLicenseUpdate(@Nullable LicenseUpdateResponse response) {
        if (response != null) {
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
            finish();
        }
    }

    private void initView() {
        mBinder.toolbarLicense.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        mBinder.toolbarLicense.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnSave.setOnClickListener(this);
        if (getIntent() != null) {
            data = getIntent().getParcelableExtra(Constants.INTENT_KEY.DATA);
            setViewData();
        }
    }

    private void setViewData() {
        if (data != null) {
            mBinder.etState.setText(data.getState());
            mBinder.etLicence.setText(data.getLicenseNumber());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (checkInputValidator()) {
                    callLicenceApi();
                }
                break;
            case R.id.ivToolBarLeft:
                finish();
                break;
        }
    }

    private boolean checkInputValidator() {
        if (TextUtils.isEmpty(mBinder.etLicence.getText().toString().trim())) {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.blank_licence_number));
            return false;
        }
        if (mBinder.etLicence.getText().toString().trim().length() > Constants.LICENCE_MAX_LENGTH) {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.licence_number_length));
            return false;
        }
        if (mBinder.etLicence.getText().toString().trim().contains(" ")) {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.licence_number_blnk_space_alert));
            return false;
        }
        if (mBinder.etLicence.getText().toString().trim().charAt(0) == '-' || mBinder.etLicence.getText().toString().trim().charAt(mBinder.etLicence.getText().toString().trim().length() - 1) == '-') {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.licence_number_hyfen_alert));
            return false;
        }
        if (TextUtils.isEmpty(mBinder.etState.getText().toString().trim())) {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.blank_state_alert));
            return false;
        }
        if (mBinder.etState.getText().toString().trim().length() >= Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(UpdateLicenseActivity.this, getString(R.string.state_max_length));
            return false;
        }
        return true;
    }

    private void callLicenceApi() {
        viewModel.updateLicence(getTextFromEditText(mBinder.etLicence),
                getTextFromEditText(mBinder.etState));
    }
}