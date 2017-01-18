package com.appster.dentamatch.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySettingsBinding;
import com.appster.dentamatch.ui.auth.ResetPasswordActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;

/**
 * Created by virender on 17/01/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    ActivitySettingsBinding settingsBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initViews();
    }

    private void initViews() {

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
                startActivity(new Intent(SettingActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.F_NAME, true));

                break;
            case R.id.tv_term_n_condition:
                startActivity(new Intent(SettingActivity.this, TermsAndConditionActivity.class).putExtra(Constants.INTENT_KEY.F_NAME, false));

                break;
        }

    }

    private void callLogoutApi() {
    }
}
