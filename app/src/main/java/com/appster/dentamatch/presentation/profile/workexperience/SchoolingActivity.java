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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SchoolsAdapter;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.databinding.ActivitySchoolingBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.model.SchoolTypeModel;
import com.appster.dentamatch.network.request.schools.PostSchoolData;
import com.appster.dentamatch.network.response.schools.SchoolingResponse;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ram on 15/01/17.
 * To inject activity reference.
 */
public class SchoolingActivity extends BaseLoadingActivity<SchoolingViewModel>
        implements View.OnClickListener, EditTextSelected {

    private ActivitySchoolingBinding mBinder;
    private SchoolsAdapter mSchoolsAdapter;
    private boolean isFromProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_schooling);
        initViews();
        getSchoolListApi();
        viewModel.getSchoolingList().observe(this, this::onSuccessSchoolingRequest);
        viewModel.getSchoolingError().observe(this, e -> mBinder.btnNext.setVisibility(View.GONE));
        viewModel.getAddSchooling().observe(this, this::onSuccessAddSchooling);
    }

    private void onSuccessAddSchooling(@Nullable BaseResponse response) {
        if (response != null) {
            if (isFromProfile) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                finish();

            } else {
                startActivity(new Intent(SchoolingActivity.this, SkillsActivity.class));
            }
        }
    }

    private void onSuccessSchoolingRequest(@Nullable SchoolingResponse response) {
        if (response != null) {
            setAdapter(response.getSchoolingResponseData().getSchoolTypeList());
            mBinder.btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        mBinder.toolbarSchooling.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSchooling.tvToolbarGeneralLeft.setText(getString(R.string.header_schooling_exp).toUpperCase());

        if (getIntent() != null) {
            isFromProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }
        if (isFromProfile) {
            mBinder.toolbarSchooling.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
            mBinder.btnNext.setText(getString(R.string.save_label));
        }
        mBinder.btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivToolBarLeft:
                hideKeyboard();
                onBackPressed();
                break;
            case R.id.btn_next:
                if (checkValidation()) {
                    hideKeyboard();
                    addSchoolListApi();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkValidation() {
        HashMap<Integer, PostSchoolData> hashMap = mSchoolsAdapter.getPostMapData();

        if (hashMap == null || hashMap.size() == 0) {
            showToast(getString(R.string.msg_choose_college));
            return false;
        } else {
            for (Map.Entry<Integer, PostSchoolData> entry : hashMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getValue().getSchoolName().trim()) && (TextUtils.isEmpty(entry.getValue().getYearOfGraduation()) || entry.getValue().getYearOfGraduation().equals("0"))) {
                    hashMap.remove(entry.getKey());
                    mSchoolsAdapter.setSchoolMapData(hashMap);
                    if (hashMap.size() == 0) {
                        showToast(getString(R.string.msg_choose_college));
                    } else {
                        checkValidation();
                        break;
                    }
                }
                if (TextUtils.isEmpty(entry.getValue().getSchoolName().trim())) {
                    showToast(getString(R.string.msg_school_name_bank));
                    return false;
                }
                if (TextUtils.isEmpty(entry.getValue().getYearOfGraduation())) {
                    showToast(getString(R.string.msg_select_year_of_graduation) + entry.getValue().getParentSchoolName());
                    return false;
                }
            }
        }
        return true;
    }


    private void setAdapter(List<SchoolTypeModel> schoolTypeList) {
        mSchoolsAdapter = new SchoolsAdapter(schoolTypeList, this, this, isFromProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSchools.setLayoutManager(layoutManager);
        mBinder.recyclerSchools.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSchools.setAdapter(mSchoolsAdapter);
        mSchoolsAdapter.notifyDataSetChanged();
    }

    private void getSchoolListApi() {
        viewModel.requestSchoolList();
    }

    private void addSchoolListApi() {
        viewModel.addSchooling(mSchoolsAdapter.getPostMapData(), mSchoolsAdapter.getList());
    }

    @Override
    public void onEditTextSelected(int position) {
        mBinder.recyclerSchools.smoothScrollToPosition(position);
    }
}
