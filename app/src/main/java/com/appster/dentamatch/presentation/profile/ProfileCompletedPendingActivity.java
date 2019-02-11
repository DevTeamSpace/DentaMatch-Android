/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.presentation.calendar.SetAvailabilityActivity;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by zishan on 28/11/17.
 * To inject activity reference.
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
            activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.approval_approved));
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.pending_approval));
            activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.profile_completion));
        } else {
            activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.approval_pending));
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.pending_approval));
            activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.profile_pending));
        }

    }

    public void completeProfile(View view) {
        Intent intentToSeatAvailability = new Intent(ProfileCompletedPendingActivity.this, SetAvailabilityActivity.class);
        intentToSeatAvailability.putExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.TRUE);
        startActivity(intentToSeatAvailability);
        finish();
    }
}
