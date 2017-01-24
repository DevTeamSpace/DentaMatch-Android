package com.appster.dentamatch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityAboutMeBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.profile.AboutMeRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

/**
 * Created by virender on 10/01/17.
 */
public class AboutMeActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAboutMeBinding mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_about_me);
        initViews();

    }

    private void initViews() {
        mBinder.toolbarAboutMe.tvToolbarGeneralLeft.setText(getString(R.string.header_about_me));
        mBinder.layoutIncludeProfileHeader.tvTitle.setText(getString(R.string.about_me_title));
        mBinder.layoutIncludeProfileHeader.tvDescription.setText(getString(R.string.desc_about_me));

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.with(getApplicationContext()).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.layoutIncludeProfileHeader.ivProfileIcon);

        }
        mBinder.layoutIncludeProfileHeader.progressBar.setProgress(100);
        mBinder.toolbarAboutMe.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnNext.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.btn_next:
                hideKeyboard();
                if (checkValidation()) {
                    postboutMeAData(prepareRequest());

                }
                break;
        }

    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(mBinder.etDescAboutMe.getText().toString())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_summary_alert));
            return false;
        }

        return true;
    }

    private AboutMeRequest prepareRequest() {
        AboutMeRequest aboutMeRequest = new AboutMeRequest();
        aboutMeRequest.setAboutMe(mBinder.etDescAboutMe.getText().toString());
        return aboutMeRequest;
    }

    private void postboutMeAData(AboutMeRequest aboutMeRequest) {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveAboutMe(aboutMeRequest).enqueue(new BaseCallback<BaseResponse>(AboutMeActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
//                    Intent intent = new Intent(AboutMeActivity.this, HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }
}
