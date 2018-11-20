/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.ProfileCompletedPendingActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;

import retrofit2.Call;

/**
 * Created by abhaykant on 08/12/17.
 * To inject activity reference.
 */

public class UserVerifyPendingActivity extends BaseActivity {
    private final String TAG = LogUtils.makeLogTag(UserVerifyPendingActivity.class);
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

        activityProfileCompletedPendingBinding.letsGoBtn.setVisibility(View.VISIBLE);
        activityProfileCompletedPendingBinding.letsGoBtn.setText(getString(R.string.lets_go));


    }

    private void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(Constants.IS_LICENCE_REQUIRED))
            isLicenceRequired = getIntent().getIntExtra(Constants.IS_LICENCE_REQUIRED, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        showProgressBar();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                checkUserVerified(false);
            }
        }, 700);

    }

    private void updateUI() {
       /* if (isLicenceRequired == 0) {
            activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.approval_approved));
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.congratulations));
            activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.profile_completion));
        }*/// else {
        activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_verify_email));
        activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.verify_email_b));
        activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.we_have_sent_a_verification));
        // }

    }

    public void completeProfile(View view) {
        checkUserVerified(true);
    }

    private void checkUserVerified(final boolean showDialog) {
        showProgressBar();
        String accessToken = PreferenceUtil.getKeyUserToken();
        LogUtils.LOGD(TAG, "accessToken>>" + accessToken);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.checkUserVerified().enqueue(new BaseCallback<UserVerifiedStatus>(UserVerifyPendingActivity.this) {
            @Override
            public void onSuccess(UserVerifiedStatus response) {
                hideProgressBar();
                if (response.getResult().getIsVerified() == Constants.USER_VERIFIED_STATUS) {

                    UserModel userModel = PreferenceUtil.getUserModel();

                    if (userModel == null) {
                        return;
                    }


                    userModel.setIsVerified(Constants.USER_VERIFIED_STATUS);
                    PreferenceUtil.setUserModel(userModel);


                    PreferenceUtil.setUserVerified(Constants.USER_VERIFIED_STATUS);

                    Intent profileCompletedIntent = new Intent(UserVerifyPendingActivity.this, ProfileCompletedPendingActivity.class);

                    if (userModel.getIsJobSeekerVerified() == Constants.JOBSEEKAR_VERIFY_STATUS) {
                        //congrates screen
                        profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, 0);

                    } else {
                        //Pending liecence screen
                        profileCompletedIntent.putExtra(Constants.IS_LICENCE_REQUIRED, 1);

                    }
                    startActivity(profileCompletedIntent);
                    finish();

                  /*  Intent intentToSeatAvailability = new Intent(UserVerifyPendingActivity.this, SetAvailabilityActivity.class);
                    intentToSeatAvailability.putExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.TRUE);
                    startActivity(intentToSeatAvailability);
                    finish();*/
                } else {


                    if (showDialog) {
                        showEmailDialog(response.getMessage());
                    }
                    //Toast.makeText(getApplicationContext(),"Please verify your email to activate account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(Call<UserVerifiedStatus> call, BaseResponse baseResponse) {

                hideProgressBar();
            }
        });

    }


    private void showEmailDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.success))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false)

                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
