/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivitySelectJobTitleBinding;
import com.appster.dentamatch.interfaces.PreferredJobListSelected;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by virender on 27/01/17.
 * To inject activity reference.
 */
public class SelectPreferredLocationActivity extends BaseLoadingActivity<SelectPreferredLocationViewModel>
        implements View.OnClickListener, PreferredJobListSelected {

    private ActivitySelectJobTitleBinding mBinder;
    private PreferredJobListAdapter mJobTitleAdapter;

    private ArrayList<PreferredJobLocationData> mPreferredJobLocationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_select_job_title);
        initViews();

        if (PreferenceUtil.getPreferredJobList() != null && PreferenceUtil.getPreferredJobList().size() > 0) {
            mJobTitleAdapter.addList(PreferenceUtil.getPreferredJobList());
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        } else {
            getPreferredLocation();
        }
        viewModel.getPreferredJobLocationModel().observe(this, this::onSuccessPreferredJobLocationResponse);
        viewModel.getPreferredJobLocationFailed().observe(this, e -> onFailedRequestPreferredJobLocation());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreferredJobLocationList != null && mPreferredJobLocationList.size() > 0) {
            mJobTitleAdapter.setSelectedListItems(mPreferredJobLocationList);
        }
    }

    private void initViews() {
        mPreferredJobLocationList = new ArrayList<>();
        mBinder.toolbarJobTitle.tvToolbarGeneralLeft.setText(getString(R.string.preferred_location_label));
        mBinder.tvCurrentLocation.setText(getString(R.string.preferred_job_location_search));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setText(getString(R.string.save_label));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setAllCaps(true);
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setOnClickListener(this);
        mBinder.toolbarJobTitle.ivToolBarLeft.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        if (getIntent().hasExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION)) {
            mPreferredJobLocationList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION);
        }

        mBinder.recyclerJobTitle.setLayoutManager(mLayoutManager);
        mBinder.recyclerJobTitle.addItemDecoration(new SimpleDividerItemDecoration(this));
        mJobTitleAdapter = new PreferredJobListAdapter(this);
        mBinder.recyclerJobTitle.setAdapter(mJobTitleAdapter);
    }

    private void getPreferredLocation(){
        viewModel.requestPreferredJobLocationList();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolBarLeft:
                onBackPressed();
                break;
            case R.id.txv_toolbar_general_right:
                if (mPreferredJobLocationList.size() == 0) {
                    showToast(getString(R.string.msg_select_pref_loc));
                } else {
                    Intent intent = getIntent();
                    intent.putExtra(Constants.EXTRA_CHOSEN_PREFERRED_JOB_LOCATION, mPreferredJobLocationList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_PREFJOB_LOC, intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPrefJobSelected(ArrayList<PreferredJobLocationData> jobTitleList) {
        mPreferredJobLocationList = jobTitleList;
    }

    private void onSuccessPreferredJobLocationResponse(@NonNull PreferredJobLocationModel response) {
        if(response.getStatus()==1) {
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
            mJobTitleAdapter.addList(response.getResult().getPreferredJobLocations());
            if (mPreferredJobLocationList != null && mPreferredJobLocationList.size() > 0) {
                mJobTitleAdapter.setSelectedListItems(mPreferredJobLocationList);
                mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
            }
        }else {
            showSnackBar(response.getMessage());
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);
        }
    }

    private void onFailedRequestPreferredJobLocation() {
        if (PreferenceUtil.getPreferredJobList() != null && PreferenceUtil.getPreferredJobList().size() > 0) {
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        } else {
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);
        }
        mJobTitleAdapter.addList(PreferenceUtil.getPreferredJobList());
    }
}
