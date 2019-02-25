/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.presentation.searchjob.SearchJobActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by virender on 10/01/17.
 * To inject activity reference.
 */
public class AboutMeActivity extends BaseLoadingActivity<AboutMeViewModel>
        implements View.OnClickListener {

    private com.appster.dentamatch.databinding.ActivityAboutMeBinding mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_about_me);
        initViews();
        viewModel.getAboutMe().observe(this, this::onSuccessSaveAboutMe);
    }

    private void onSuccessSaveAboutMe(@Nullable BaseResponse response) {
        if (response != null) {
            PreferenceUtil.setProfileCompleted(true);
            Intent intent = new Intent(AboutMeActivity.this, SearchJobActivity.class)
                    .putExtra(Constants.EXTRA_IS_FIRST_TIME, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mBinder.toolbarAboutMe.tvToolbarGeneralLeft.setText(getString(R.string.header_about_me));
        mBinder.layoutIncludeProfileHeader.tvTitle.setText(getString(R.string.about_me_title));
        mBinder.layoutIncludeProfileHeader.tvDescription.setText(getString(R.string.desc_about_me));

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.get().load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(mBinder.layoutIncludeProfileHeader.ivProfileIcon);

        }

        mBinder.layoutIncludeProfileHeader.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.COMPLETE);
        mBinder.toolbarAboutMe.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnNext.setOnClickListener(this);

        mBinder.etDescAboutMe.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
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
                    postAboutMeAData();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(mBinder.etDescAboutMe.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_profile_summary_alert));
            return false;
        }
        return true;
    }

    private void postAboutMeAData() {
        viewModel.saveAboutMe(getTextFromEditText(mBinder.etDescAboutMe));
    }
}