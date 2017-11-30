package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by zishan on 28/11/17.
 */

public class ProfileCompletedPendingActivity extends BaseActivity {
    private String TAG = "ProfileCompletedPendingActivity";
    private int isLicenceRequired;
    private ActivityProfileCompletedPendingBinding activityProfileCompletedPendingBinding;

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileCompletedPendingBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_completed_pending);
        getIntentData();
        updateUI();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(Constants.IS_LICENCE_REQUIRED))
            isLicenceRequired = getIntent().getIntExtra(Constants.IS_LICENCE_REQUIRED, 0);
    }

    private void updateUI() {
        if (isLicenceRequired == 0) {
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.congratulations));
            activityProfileCompletedPendingBinding.messageOne.setText(getResources().getString(R.string.profile_completion));
        } else {
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.pending_approval));
            activityProfileCompletedPendingBinding.messageOne.setText(getResources().getString(R.string.profile_pending));
        }

    }

}
