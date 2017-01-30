package com.appster.dentamatch.ui.searchjob;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySelectJobTitleBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import retrofit2.Call;

/**
 * Created by virender on 27/01/17.
 */
public class SelectJobTitleActivity extends BaseActivity {
    private final String TAG = "SelectJobTitleActivity";
    private ActivitySelectJobTitleBinding mBinder;
    private LinearLayoutManager mLayoutManager;
    private JobTitleAdapter mJobTitleAdapter;


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
        mBinder.toolbarJobTitle.tvToolbarGeneralLeft.setText(getString(R.string.job_title));
        mLayoutManager = new LinearLayoutManager(this);
//
        mBinder.recyclerAjobTitle.setLayoutManager(mLayoutManager);
        mBinder.recyclerAjobTitle.addItemDecoration(new SimpleDividerItemDecoration(this));
        mJobTitleAdapter = new JobTitleAdapter(this);
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

    private void setAdapter() {

    }
}
