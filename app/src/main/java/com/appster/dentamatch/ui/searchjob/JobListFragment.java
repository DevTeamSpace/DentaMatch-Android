/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.searchjob;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.JobListAdapter;
import com.appster.dentamatch.databinding.FragmentJobListBinding;
import com.appster.dentamatch.eventbus.JobDataReceivedEvent;
import com.appster.dentamatch.eventbus.SaveUnSaveEvent;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.base.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Appster on 24/01/17.
 * User Interface to view job listing screen.
 */

public class JobListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentJobListBinding mJobListBinding;
    private ArrayList<SearchJobModel> mJobListData;
    private boolean mIsPaginationNeeded;
    private LinearLayoutManager mLayoutManager;
    private JobListAdapter mJobAdapter;

    public static JobListFragment newInstance() {
        return new JobListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    /**
     * This is the method which is returned from SearchJobDataHelper class whenever requestData()
     * method is called or updateDataViaPagination() method is called or refresh method is called.
     * This method contains event which has the updated joblist array with handling based on
     * pagination already done from the Helper class. We need to simply clear the joblist array
     * present in this class and add the value of list from event to it.
     * The event also contains the total results count as well as the mIsPaginationNeeded boolean
     * value evaluated beforehand .
     *
     * @param event: the event class with jobList, mIsPaginationNeeded boolean and total result count.
     */
    @Subscribe
    public void onDataUpdated(JobDataReceivedEvent event) {
        if (event != null) {
            mJobListData.clear();
            mJobListData.addAll(event.getJobList());
            mIsPaginationNeeded = event.isPaginationNeeded();

            if (mJobListData.size() > 0) {
                mJobListBinding.tvNoDataFound.setVisibility(View.GONE);
                mJobListBinding.tvJobResultCount.setVisibility(View.VISIBLE);
                if (event.getTotalItem() == 1) {
                    mJobListBinding.tvJobResultCount.setText(String.valueOf(event.getTotalItem()).concat(" result found"));

                } else {
                    mJobListBinding.tvJobResultCount.setText(String.valueOf(event.getTotalItem()).concat(" results found"));
                }
                if (event.getIsJobSeekerVerified() == Constants.USER_VERIFIED_STATUS && event.getProfileCompleted() == Constants.USER_VERIFIED_STATUS) {
                    mJobListBinding.tvProfileStatus.setVisibility(View.GONE);
                } else if (event.getIsJobSeekerVerified() != Constants.USER_VERIFIED_STATUS) {
                    mJobListBinding.tvProfileStatus.setVisibility(View.VISIBLE);
                    if (getActivity() != null)
                        mJobListBinding.tvProfileStatus.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_yellow));
                    mJobListBinding.tvProfileStatus.setText(getString(R.string.your_job_profile_status));

                } else if (/*event.getIsJobSeekerVerified()!= Constants.USER_VERIFIED_STATUS &&*/ event.getProfileCompleted() != Constants.USER_VERIFIED_STATUS) {
                    mJobListBinding.tvProfileStatus.setVisibility(View.VISIBLE);
                    if (getActivity() != null)
                        mJobListBinding.tvProfileStatus.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_color));
                    mJobListBinding.tvProfileStatus.setText(getString(R.string.your_profile_incomplete));

                }

                UserModel userModel = PreferenceUtil.getUserModel();
                if (userModel != null) {
                    userModel.setProfileCompleted(event.getProfileCompleted());
                    PreferenceUtil.setUserModel(userModel);
                }

                mJobAdapter.notifyDataSetChanged();

            } else {
                mJobListBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                mJobListBinding.tvJobResultCount.setVisibility(View.GONE);
            }

            /*
              Hide pagination loader if it is visible.
             */
            if (mJobListBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
                mJobListBinding.layJobListPagination.setVisibility(View.GONE);
            }

            /*
              Stop refreshing if the swipe loader is refreshing.
             */
            if (mJobListBinding.swipeRefreshJobList.isRefreshing()) {
                mJobListBinding.swipeRefreshJobList.setRefreshing(false);
            }
        }
    }

    @Subscribe
    public void onJobSavedUnsaved(SaveUnSaveEvent event) {
        if (event != null) {

            for (SearchJobModel model : mJobListData) {

                if (model.getId() == event.getJobID()) {
                    model.setIsSaved(event.getStatus());
                    mJobAdapter.updateData(model);
                    SearchJobDataHelper.getInstance().notifyItemsChanged(model);
                    break;
                }
            }
        }
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_list, container, false);
        initViews();
        mJobListBinding.rvJobs.setLayoutManager(mLayoutManager);
        mJobListBinding.rvJobs.setAdapter(mJobAdapter);
        mJobListBinding.rvJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });

        return mJobListBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
          Request data helper to provide data for user's filter set.
         */
        SearchJobDataHelper.getInstance().requestData(getActivity());
    }

    private void initViews() {
        mJobListData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mJobAdapter = new JobListAdapter(getActivity(), mJobListData);
        mJobListBinding.swipeRefreshJobList.setColorSchemeResources(R.color.colorAccent);
        mJobListBinding.swipeRefreshJobList.setOnRefreshListener(this);


    }

    /**
     * if the recycler view has reached its last item , update data via pagination if required.
     */
    private void checkIfItsLastItem() {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (mIsPaginationNeeded) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mIsPaginationNeeded = false;
                mJobListBinding.layJobListPagination.setVisibility(View.VISIBLE);
                SearchJobDataHelper.getInstance().updateDataViaPagination(getActivity());
            }
        }
    }

    @Override
    public void onRefresh() {
        SearchJobDataHelper.getInstance().requestRefreshData(getActivity());
    }


}
