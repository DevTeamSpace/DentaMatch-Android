package com.appster.dentamatch.ui.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.calendar.SetAvailabilityActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.UpdateProfileActivity;
import com.appster.dentamatch.util.Constants;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by abhaykant on 08/12/17.
 */

public class UserVerifyPendingActivity extends BaseActivity {
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

        activityProfileCompletedPendingBinding.letsGoBtn.setVisibility(View.VISIBLE);
        activityProfileCompletedPendingBinding.letsGoBtn.setText(getString(R.string.verified_email));
        checkUserVerified();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(Constants.IS_LICENCE_REQUIRED))
            isLicenceRequired = getIntent().getIntExtra(Constants.IS_LICENCE_REQUIRED, 0);
    }

    private void updateUI() {
       /* if (isLicenceRequired == 0) {
            activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.approval_approved));
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.congratulations));
            activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.profile_completion));
        }*/// else {
            activityProfileCompletedPendingBinding.imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_verify_email));
            activityProfileCompletedPendingBinding.headingMessage.setText(getResources().getString(R.string.verify_email));
            activityProfileCompletedPendingBinding.message.setText(getResources().getString(R.string.we_have_sent_a_verification));
       // }

    }

    public void completeProfile(View view) {
        checkUserVerified();
    }

    private void checkUserVerified(){
       showProgressBar();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.checkUserVerified().enqueue(new BaseCallback<UserVerifiedStatus>(UserVerifyPendingActivity.this) {
            @Override
            public void onSuccess(UserVerifiedStatus response) {
                hideProgressBar();
                if(response.getResult().getIsVerified()==Constants.USER_VERIFIED_STATUS) {
                    Intent intentToSeatAvailability = new Intent(UserVerifyPendingActivity.this, SetAvailabilityActivity.class);
                    intentToSeatAvailability.putExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.TRUE);
                    startActivity(intentToSeatAvailability);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please verify your email to activate account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(Call<UserVerifiedStatus> call, BaseResponse baseResponse) {

                hideProgressBar();
            }
        });

    }
}
