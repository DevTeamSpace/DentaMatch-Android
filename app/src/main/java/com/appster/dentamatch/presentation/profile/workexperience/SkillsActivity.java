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
import android.view.View;
import android.widget.Button;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SkillsAdapter;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivitySkillsBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.interfaces.OnSkillClick;
import com.appster.dentamatch.model.ParentSkillModel;
import com.appster.dentamatch.model.SubSkillModel;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.skills.SkillsResponse;
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 * To inject activity reference.
 */
public class SkillsActivity extends BaseLoadingActivity<SkillsViewModel>
        implements View.OnClickListener, OnSkillClick, EditTextSelected {

    private ActivitySkillsBinding mBinder;
    private SkillsAdapter mSkillsAdapter;
    private int mSkillPosition;
    private boolean isFromProfile;
    private List<ParentSkillModel> mParentSkillList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        initViews();
        getSkillsListApi();
        viewModel.getSkills().observe(this, this::onSuccessSkillsRequest);
        viewModel.getSkillsError().observe(this, e -> mBinder.btnNext.setVisibility(View.GONE));
        viewModel.getSkillsUpdate().observe(this, this::onSuccessSkillsUpdate);
    }

    private void onSuccessSkillsUpdate(@Nullable BaseResponse response) {
        if (response != null) {
            if (isFromProfile) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                finish();
            } else {
                startActivity(new Intent(SkillsActivity.this, AffiliationActivity.class));
            }
        }
    }

    private void onSuccessSkillsRequest(@Nullable SkillsResponse response) {
        if (response != null) {
            mParentSkillList = response.getSkillsResponseData().getSkillsList();
            setAdapter(mParentSkillList);
            mBinder.btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        Button btnNext = mBinder.btnNext;
        mBinder.toolbarSkills.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_skills_exp).toUpperCase());
        if (getIntent() != null) {
            isFromProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }
        if (isFromProfile) {
            mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
            mBinder.btnNext.setText(getString(R.string.save_label));
        }
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;
            case R.id.btn_next:
                updateSkillsListApi();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<SubSkillModel> subSkills = data.getParcelableArrayListExtra(Constants.BundleKey.SUB_SKILLS);
            mParentSkillList.get(mSkillPosition).setSubSkills(subSkills);
            mSkillsAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapter(List<ParentSkillModel> skillArrayList) {
        mSkillsAdapter = new SkillsAdapter(skillArrayList, this, this, this, isFromProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSkills.setLayoutManager(layoutManager);
        mBinder.recyclerSkills.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSkills.setAdapter(mSkillsAdapter);
        mSkillsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(ArrayList<SubSkillModel> subSkillList, int position) {
        Intent intent = new Intent(SkillsActivity.this, SubSkillsActivity.class);
        intent.putExtra(Constants.BundleKey.SUB_SKILLS, subSkillList);
        startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_SKILLS);
        mSkillPosition = position;
    }

    private void getSkillsListApi() {
        viewModel.requestSkillsList();
    }

    private void updateSkillsListApi() {
        viewModel.updateSkills(mParentSkillList);
    }

    @Override
    public void onEditTextSelected(int position) {
        mBinder.recyclerSkills.smoothScrollToPosition(position);
    }
}