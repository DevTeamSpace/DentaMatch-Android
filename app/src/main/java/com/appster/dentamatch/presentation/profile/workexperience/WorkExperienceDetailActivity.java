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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityWorkExperienceDetailBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

/**
 * Created by virender on 05/01/17.
 * To inject activity reference.
 */
//TODO REFACTOR This activity don't have any calls
public class WorkExperienceDetailActivity extends BaseLoadingActivity<WorkExperienceDetailViewModel>
        implements View.OnClickListener, YearSelectionListener {

    private ActivityWorkExperienceDetailBinding mBinder;
    private int expMonth = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experience_detail);
        initViews();
        viewModel.getWorkExpResponse().observe(this, this::onSuccessWorkExperienceResponse);
    }

    private void onSuccessWorkExperienceResponse(@Nullable WorkExpResponse response) {
        if (response != null) {
            startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));
            finish();
        }
    }

    private void initViews() {
        mBinder.toolbarWorkExpDetail.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.btnNextDetailWorkExp.setOnClickListener(this);
        mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setOnClickListener(this);
        mBinder.toolbarWorkExpDetail.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeReference2.tvReferenceDelete.setOnClickListener(this);

        String yearLabel, monthLabel;

        Integer year = PreferenceUtil.getYear();
        Integer month = PreferenceUtil.getMonth();

        if (year != null && month != null) {
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
            String workExp = String.valueOf(year)
                    .concat(" ")
                    .concat(yearLabel)
                    .concat(" ")
                    .concat(String.valueOf(month))
                    .concat(" ")
                    .concat(monthLabel);
            mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setText(workExp);
            expMonth = year * 12 + month;
        }
        mBinder.includeLayoutWorkExp.etOfficeName.setText(PreferenceUtil.getOfficeName());
        mBinder.includeLayoutWorkExp.etOfficeAddress.requestFocus();
    }

    @Override
    public void onClick(View view) {
        int jobTitleId = 0;
        switch (view.getId()) {
            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeReference2.tvReferenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeReference2.tvReferenceCount.setText(getString(R.string.reference2));
                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ivToolBarLeft:
                onBackPressed();
                break;
            case R.id.tv_reference_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
                mBinder.layoutReference2.setVisibility(View.GONE);
                break;
            case R.id.tv_add_more_experience:
                hideKeyboard();
                callAddExpApi(WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                        Constants.APIS.ACTION_ADD,
                        jobTitleId,
                        expMonth,
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName),
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress),
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity),
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeState),
                        Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceName),
                        Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceMobile),
                        Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceName),
                        Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceMobile)));
                break;
            case R.id.tv_experience_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.includeLayoutWorkExp.tvExperienceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeLayoutWorkExp.tvExperienceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }
                new BottomSheetPicker(this, this, year, month);
                break;
            case R.id.btn_next_detail_work_exp:
                Utils.showToast(WorkExperienceDetailActivity.this, "New feature will introduce soon");
                startActivity(new Intent(this, AffiliationActivity.class));
                break;
            default:
                break;
        }
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
        String workExp = String.valueOf(year)
                .concat(" ")
                .concat(yearLabel)
                .concat(" ")
                .concat(String.valueOf(month))
                .concat(" ")
                .concat(monthLabel);
        mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setText(workExp);
        expMonth = year * 12 + month;
    }

    private void callAddExpApi(WorkExpRequest workExpRequest) {
        viewModel.addWorkExp(workExpRequest);
    }
}
