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
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.profile.JobTitleList;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 27/01/17.
 */
public class SelectJobTitleActivity extends BaseActivity implements View.OnClickListener, JobTitleSelected {
    private final String TAG = "SelectJobTitleActivity";
    private ActivitySelectJobTitleBinding mBinder;
    private LinearLayoutManager mLayoutManager;
    private JobTitleAdapter mJobTitleAdapter;
    private ArrayList<JobTitleList> mSelectedTitleList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_select_job_title);
        initViews();

        if (PreferenceUtil.getSearchJobTitleList() != null && PreferenceUtil.getSearchJobTitleList().size() > 0) {
            mJobTitleAdapter.addList(PreferenceUtil.getSearchJobTitleList());
        } else {
            callJobListApi();
        }
    }

    private void initViews() {
        mSelectedTitleList = new ArrayList<>();
        mBinder.toolbarJobTitle.tvToolbarGeneralLeft.setText(getString(R.string.job_title));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setText(getString(R.string.save_label));
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setAllCaps(true);
        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setOnClickListener(this);
        mBinder.toolbarJobTitle.ivToolBarLeft.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
//
        mBinder.recyclerAjobTitle.setLayoutManager(mLayoutManager);
        mBinder.recyclerAjobTitle.addItemDecoration(new SimpleDividerItemDecoration(this));
        mJobTitleAdapter = new JobTitleAdapter(this, this);
        mBinder.recyclerAjobTitle.setAdapter(mJobTitleAdapter);
    }

    @Override
    public String getActivityName() {
        return null;
    }


    private void callJobListApi() {
        LogUtils.LOGD(TAG, "job title list");
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.jobTitle().enqueue(new BaseCallback<JobTitleResponse>(SelectJobTitleActivity.this) {
            @Override
            public void onSuccess(JobTitleResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    LogUtils.LOGD(TAG, "Size is--=" + response.getJobTitleResponseData().getJobTitleList().size());

                    PreferenceUtil.setSearchJobTitleList(response.getJobTitleResponseData().getJobTitleList());
                    mJobTitleAdapter.addList(response.getJobTitleResponseData().getJobTitleList());

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());

                }
            }

            @Override
            public void onFail(Call<JobTitleResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
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
                if(mSelectedTitleList.size() == 0) {
                    showToast("Please select at least one job to proceed");
                }else{
                    Intent intent = getIntent();
                    intent.putExtra(Constants.EXTRA_CHOSEN_JOB_TITLES,mSelectedTitleList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_JOB_TITLE,intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onJobTitleSelected(ArrayList<JobTitleList> jobTitleList) {
        mSelectedTitleList = jobTitleList;
    }
}
