package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityUpdateLicenseBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.response.profile.LicenceUpdateResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

/**
 * Created by virender on 20/01/17.
 * To inject activity reference.
 */
public class UpdateLicenseActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = LogUtils.makeLogTag(UpdateLicenseActivity.class);
    private ActivityUpdateLicenseBinding mBinder;
    private LicenceRequest data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_update_license);
        initView();

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
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (checkInputValidator()) {
                    callLicenceApi(prepareLicenceRequest());
                }
                break;
            case R.id.iv_tool_bar_left:
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

    private LicenceRequest prepareLicenceRequest() {
        processToShowDialog();
        LicenceRequest licenceRequest = new LicenceRequest();
        licenceRequest.setLicense(mBinder.etLicence.getText().toString());
        licenceRequest.setState(mBinder.etState.getText().toString());
        return licenceRequest;
    }

    private void callLicenceApi(LicenceRequest licenceRequest) {
        try {
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.updateLicence(licenceRequest).enqueue(new BaseCallback<LicenceUpdateResponse>(UpdateLicenseActivity.this) {
                @Override
                public void onSuccess(LicenceUpdateResponse response) {
                    if (response.getStatus() == 1) {
                        EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                        finish();
                    } else {
                        Utils.showToast(getApplicationContext(), response.getMessage());

                    }
                }

                @Override
                public void onFail(Call<LicenceUpdateResponse> call, BaseResponse baseResponse) {

                }
            });
        } catch (Exception e) {
            LogUtils.LOGE(TAG,e.getMessage());


        }finally {
            hideProgressBar();

        }
    }
}
