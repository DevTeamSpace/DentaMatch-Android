package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySelectJobTitleBinding;
import com.appster.dentamatch.interfaces.PreferredJobListSelected;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 27/01/17.
 * To inject activity reference.
 */
public class SelectPreferredLocationActivity extends BaseActivity implements View.OnClickListener, PreferredJobListSelected {
    private final String TAG = "SelectJobTitleActivity";
    private ActivitySelectJobTitleBinding mBinder;
    private PreferredJobListAdapter mJobTitleAdapter;
    //private ArrayList<JobTitleListModel> mSelectedTitleList;

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

    @Override
    public String getActivityName() {
        return null;
    }

    private void getPreferredLocation(){
         showProgressBar();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.getPreferredJobLocationList().enqueue(new BaseCallback<PreferredJobLocationModel>(SelectPreferredLocationActivity.this) {
            @Override
            public void onSuccess(PreferredJobLocationModel response) {
                hideProgressBar();
                if(response.getStatus()==1) {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);


                    ArrayList<PreferredJobLocationData>  mPreferredJobLoc = (ArrayList<PreferredJobLocationData>) response.getResult().getPreferredJobLocations();

                    PreferenceUtil.setPreferredJobList(mPreferredJobLoc);

                    mJobTitleAdapter.addList(mPreferredJobLoc);

                    if (mPreferredJobLocationList != null && mPreferredJobLocationList.size() > 0) {
                       mJobTitleAdapter.setSelectedListItems(mPreferredJobLocationList);
                        mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
                    }
                }else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFail(Call<PreferredJobLocationModel> call, BaseResponse baseResponse) {
                hideProgressBar();
                if (PreferenceUtil.getPreferredJobList() != null && PreferenceUtil.getPreferredJobList().size() > 0) {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
                } else {
                    mBinder.toolbarJobTitle.txvToolbarGeneralRight.setVisibility(View.GONE);
                }
                mJobTitleAdapter.addList(PreferenceUtil.getPreferredJobList());
            }
        });
    }


    /*private void callJobListApi() {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.jobTitle().enqueue(new BaseCallback<JobTitleResponse>(SelectPreferredLocationActivity.this) {
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

    }*/

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
}
