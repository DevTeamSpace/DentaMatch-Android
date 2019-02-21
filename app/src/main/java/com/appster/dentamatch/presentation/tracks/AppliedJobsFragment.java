/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.tracks;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.TrackJobsAdapter;
import com.appster.dentamatch.base.BaseLoadingFragment;
import com.appster.dentamatch.eventbus.JobCancelEvent;
import com.appster.dentamatch.eventbus.TrackJobListRetrievedEvent;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.presentation.searchjob.JobListViewModel;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Appster on 02/02/17.
 * Fragment to render Applied job user interface.
 */

public class AppliedJobsFragment extends BaseLoadingFragment<AppliedJobsViewModel>
        implements SwipeRefreshLayout.OnRefreshListener, TrackJobsAdapter.UnSaveJobListener {

    private com.appster.dentamatch.databinding.FragmentAppliedJobsBinding mBinding;
    private LinearLayoutManager mLayoutManager;
    private TrackJobsAdapter mJobAdapter;
    private ArrayList<SearchJobModel> mJobListData;

    public static com.appster.dentamatch.presentation.tracks.AppliedJobsFragment newInstance() {
        return new com.appster.dentamatch.presentation.tracks.AppliedJobsFragment();
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
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_applied_jobs, container, false);
        initViews();
        mBinding.rvFragmentAppliedJobs.setLayoutManager(mLayoutManager);
        mBinding.rvFragmentAppliedJobs.setAdapter(mJobAdapter);
        mBinding.rvFragmentAppliedJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });
        viewModel.getCancelJob().observe(this, this::onSuccessCancelJob);
        viewModel.getUnSaveJob().observe(this, this::onSuccessUnSaveJob);
        viewModel.getUnSaveJobFailed().observe(this, this::onFailedUnSaveJob);
        return mBinding.getRoot();
    }

    private void onFailedUnSaveJob(@Nullable JobListViewModel.SaveUnSaveJobResult result) {
        if (result != null) {
            mJobAdapter.onFailedCancel(result);
        }
    }

    private void onSuccessUnSaveJob(@Nullable JobListViewModel.SaveUnSaveJobResult result) {
        if (result != null) {
            mJobAdapter.onSuccessCancel(result);
        }
    }

    private void onSuccessCancelJob(@Nullable Integer id) {
        if (id != null) {
            mJobAdapter.cancelJob(id);
            if (mJobListData.size() > 0) {
                mBinding.tvNoJobs.setVisibility(View.GONE);
            } else {
                mBinding.tvNoJobs.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isActive() && isVisible()) {
            TrackJobsDataHelper.getInstance().requestData(getActivity(), Constants.SEARCHJOBTYPE.APPLIED.getValue());
        }
    }

    private void initViews() {
        mJobListData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mJobAdapter = new TrackJobsAdapter(getActivity(), mJobListData, false, true, this);
        mBinding.swipeRefreshJobList.setColorSchemeResources(R.color.colorAccent);
        mBinding.swipeRefreshJobList.setOnRefreshListener(this);
    }

    /**
     * if the recycler view has reached its last item , update data via pagination if required.
     */
    private void checkIfItsLastItem() {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (TrackJobsDataHelper.getInstance().isPaginationNeeded(Constants.SEARCHJOBTYPE.APPLIED.getValue())) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mBinding.layJobListPagination.setVisibility(View.VISIBLE);
                TrackJobsDataHelper.getInstance().requestPaginatedData(getActivity(), Constants.SEARCHJOBTYPE.APPLIED.getValue());
            }
        }
    }

    @Subscribe
    public void onDataUpdated(TrackJobListRetrievedEvent event) {
        if (event != null) {
            if (event.getType() == Constants.SEARCHJOBTYPE.APPLIED.getValue()) {
                mJobListData.clear();
                mJobListData.addAll(event.getData());
                if (mJobListData.size() == 0) {
                    mBinding.tvNoJobs.setVisibility(View.VISIBLE);
                } else {
                    mBinding.tvNoJobs.setVisibility(View.GONE);
                }
                mJobAdapter.notifyDataSetChanged();
            }
                /*
                  Hide pagination loader if it is visible.
                 */
            if (mBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
                mBinding.layJobListPagination.setVisibility(View.GONE);
            }
                /*
                  Stop refreshing if the swipe loader is refreshing.
                 */
            if (mBinding.swipeRefreshJobList.isRefreshing()) {
                mBinding.swipeRefreshJobList.setRefreshing(false);
            }
        }
    }

    private void cancelJob(final int id, String msg) {
        viewModel.cancelJob(id, msg);
    }

    @Subscribe
    public void jobCancelled(JobCancelEvent event) {
        if (event != null) {
            for (SearchJobModel model : mJobListData) {

                if (model.getId() == event.getJobID()) {
                    cancelJob(event.getJobID(), event.getMsg());
                    break;
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        TrackJobsDataHelper.getInstance().refreshData(getActivity(), Constants.SEARCHJOBTYPE.APPLIED.getValue());
    }

    @Override
    public void unSaveJob(int id, int position) {
        viewModel.unSaveJob(id, position);
    }
}

