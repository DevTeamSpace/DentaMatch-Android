/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySelectJobTitleBinding;
import com.appster.dentamatch.interfaces.JobTitleSelected;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;

/**
 * Created by virender on 27/01/17.
 * To inject activity reference.
 */
public class SelectJobTitleActivity extends BaseActivity implements View.OnClickListener, JobTitleSelected {
    private final String TAG = "SelectJobTitleActivity";
    private ActivitySelectJobTitleBinding mBinder;
    private JobTitleAdapter mJobTitleAdapter;
    private ArrayList<JobTitleListModel> mSelectedTitleList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_select_job_title);
        initViews();

        if (PreferenceUtil.getSearchJobTitleList() != null && PreferenceUtil.getSearchJobTitleList().size() > 0) {
            mJobTitleAdapter.addList(PreferenceUtil.getSearchJobTitleList());
            mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        } else {
            callJobListApi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSelectedTitleList != null && mSelectedTitleList.size() > 0) {
            mJobTitleAdapter.setSelectedListItems(mSelectedTitleList);
        }
    }

    private void initViews() {
        mSelectedTitleList = new ArrayList<>();
        mBinder.toolbarJobTitle.tvToolbarGeneralLeft.setText(getString(R.string.job_title));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setText(getString(R.string.save_label));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setAllCaps(true);
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setOnClickListener(this);
        mBinder.toolbarJobTitle.ivToolBarLeft.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        if (getIntent().hasExtra(Constants.EXTRA_CHOSEN_JOB_TITLES)) {
            mSelectedTitleList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_CHOSEN_JOB_TITLES);
        }

        mBinder.recyclerJobTitle.setLayoutManager(mLayoutManager);
        mBinder.recyclerJobTitle.addItemDecoration(new SimpleDividerItemDecoration(this));
        mJobTitleAdapter = new JobTitleAdapter(this);
        mBinder.recyclerJobTitle.setAdapter(mJobTitleAdapter);
    }

    @Override
    public String getActivityName() {
        return null;
    }


    private void callJobListApi() {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.jobTitle().enqueue(new BaseCallback<JobTitleResponse>(SelectJobTitleActivity.this) {
            @Override
            public void onSuccess(JobTitleResponse response) {
                if (response.getStatus() == 1) {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
                    ArrayList<JobTitleListModel> jobTitles = response.getJobTitleResponseData().getJobTitleList();

                    for (int i = 0; i < jobTitles.size(); i++) {
                        if (jobTitles.get(i).getJobTitle().equalsIgnoreCase(Constants.OTHERS)) {

                            if (i != jobTitles.size() - 1) {
                                Collections.swap(jobTitles, i, jobTitles.size() - 1);
                            }

                            break;
                        }
                    }

                    PreferenceUtil.setSearchJobTitleList(jobTitles);
                    mJobTitleAdapter.addList(jobTitles);

                    if (mSelectedTitleList != null && mSelectedTitleList.size() > 0) {
                        mJobTitleAdapter.setSelectedListItems(mSelectedTitleList);
                        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
                    }

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFail(Call<JobTitleResponse> call, BaseResponse baseResponse) {
                if (PreferenceUtil.getSearchJobTitleList() != null && PreferenceUtil.getSearchJobTitleList().size() > 0) {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
                } else {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);
                }
                mJobTitleAdapter.addList(PreferenceUtil.getSearchJobTitleList());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.txv_toolbar_general_right:
                if (mSelectedTitleList.size() == 0) {
                    showToast(getString(R.string.msg_select_job));
                } else {
                    Intent intent = getIntent();
                    intent.putExtra(Constants.EXTRA_CHOSEN_JOB_TITLES, mSelectedTitleList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE, intent);
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onJobTitleSelected(ArrayList<JobTitleListModel> jobTitleList) {
        mSelectedTitleList = jobTitleList;
    }
}
