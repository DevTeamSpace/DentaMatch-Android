/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus;
import com.appster.dentamatch.presentation.profile.ProfileCompletedPendingActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

/**
 * Created by abhaykant on 08/12/17.
 * To inject activity reference.
 */

public class UserVerifyPendingActivity extends BaseLoadingActivity<UserVerifyPendingViewModel> {

    private ActivityProfileCompletedPendingBinding activityProfileCompletedPendingBinding;
    private boolean mShowDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileCompletedPendingBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_completed_pending);
        updateUI();

        activityProfileCompletedPendingBinding.letsGoBtn.setVisibility(View.VISIBLE);
        activityProfileCompletedPendingBinding.letsGoBtn.setText(getString(R.string.lets_go));

        viewModel.getUserVerified().observe(this, this::onSuccessUserVerified);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> checkUserVerified(false), 700);
    }

    private void updateUI() {
        activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_verify_email));
        activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.verify_email_b));
        activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.we_have_sent_a_verification));
    }

    public void completeProfile(View view) {
        checkUserVerified(true);
    }

    private void checkUserVerified(final boolean showDialog) {
        mShowDialog = showDialog;
        viewModel.checkUserVerified();
    }

    private void showEmailDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.success))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss()).setCancelable(false)
                .show();
    }

    private void onSuccessUserVerified(@Nullable UserVerifiedStatus response) {
        if (response != null) {
            if (response.getResult().getIsVerified() == Constants.USER_VERIFIED_STATUS) {
                Intent profileCompletedIntent = new Intent(UserVerifyPendingActivity.this, ProfileCompletedPendingActivity.class);
                UserModel userModel = PreferenceUtil.getUserModel();
                if (userModel == null) {
                    return;
                }
                if (userModel.getIsJobSeekerVerified() == Constants.JOBSEEKAR_VERIFY_STATUS) {
                    //congrates screen
                    profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, 0);

                } else {
                    //Pending liecence screen
                    profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, 1);
                }
                startActivity(profileCompletedIntent);
                finish();
            } else {
                if (mShowDialog) {
                    showEmailDialog(response.getMessage());
                }
            }
        }
    }
}
