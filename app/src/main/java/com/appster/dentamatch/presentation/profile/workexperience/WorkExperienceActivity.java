/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperienceBinding;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by virender on 04/01/17.
 * To inject activity reference.
 */
public class WorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {
    private ActivityWorkExperienceBinding mBinder;
    private String selectedJobtitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experience);
        initViews();
    }

    private void initViews() {
        hideKeyboard();
        hideKeyboard(mBinder.etOfficeName);

        mBinder.toolbarWorkExp.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.WORK_EXPERIENCE);
        mBinder.toolbarWorkExp.ivToolBarLeft.setOnClickListener(this);
        mBinder.etJobTitle.setOnClickListener(this);
        mBinder.btnNextWorkExp.setOnClickListener(this);
        mBinder.tvExperienceWorkExp.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.get()
                    .load(PreferenceUtil.getProfileImagePath())
                    .centerCrop()
                    .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinder.createProfileIvProfileIcon);
        }

        selectedJobtitle = PreferenceUtil.getJobTitle();

        if (!TextUtils.isEmpty(selectedJobtitle)) {
            mBinder.etJobTitle.setText(selectedJobtitle);
        }

        mBinder.layoutDummy.requestFocus();
    }


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_next_work_exp:
                if (checkValidation()) {
                    PreferenceUtil.setOfficeName(Utils.getStringFromEditText(mBinder.etOfficeName));
                    hideKeyboard();
                    startActivity(new Intent(this, MyWorkExpListActivity.class));
                }
                break;

            case R.id.ivToolBarLeft:
                hideKeyboard();
                onBackPressed();
                break;

            case R.id.et_job_title:
                hideKeyboard();
                new BottomSheetJobTitle(WorkExperienceActivity.this, this, PreferenceUtil.getJobTitlePosition());
                break;

            case R.id.tv_experience_work_exp:
                hideKeyboard();
                new BottomSheetPicker(this, this, 0, 0);
                break;

            default:
                break;
        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(selectedJobtitle)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }

        if (TextUtils.isEmpty(mBinder.tvExperienceWorkExp.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_year_alert));
            return false;
        }

        if (TextUtils.isEmpty(mBinder.etOfficeName.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_office_name_alert));
            return false;
        }

        if (mBinder.etOfficeName.getText().toString().trim().length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.office_name_length_alert));
            return false;
        }

        return true;
    }

    @Override
    public void onExperienceSection(int year, int month) {
        String yearLabel, monthLabel;

        if (year == 1) {
            yearLabel = getString(R.string.txt_single_year);
        } else {
            yearLabel = getString(R.string.txt_multiple_years);
        }

        if (month == 1) {
            monthLabel = getString(R.string.txt_single_month);
        } else {
            monthLabel = getString(R.string.txt_multiple_months);
        }

        mBinder.tvExperienceWorkExp.setText(year + " " + yearLabel + " " + month + " " + monthLabel);
        PreferenceUtil.setMonth(month);
        PreferenceUtil.setYear(year);
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired) {
        selectedJobtitle = title;
        PreferenceUtil.setJobTitle(title);
        PreferenceUtil.setJobTitleId(titleId);
        PreferenceUtil.setJobTitlePosition(position);
        mBinder.etJobTitle.setText(title);
    }
}
