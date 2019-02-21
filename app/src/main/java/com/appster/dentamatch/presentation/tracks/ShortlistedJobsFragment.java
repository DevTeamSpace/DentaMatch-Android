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
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.presentation.searchjob.JobListViewModel;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Pair;

/**
 * Created by Appster on 02/02/17.
 * Fragment to render user interface for shortlisted jobs.
 */

public class ShortlistedJobsFragment extends BaseLoadingFragment<ShortlistedJobsViewModel>
        implements SwipeRefreshLayout.OnRefreshListener, TrackJobsAdapter.UnSaveJobListener {

    private com.appster.dentamatch.databinding.FragmentShortlistedJobsBinding mBinding;
    private static final String DATA_ARRAY = "DATA_ARRAY";
    private int mPage = 1;
    private boolean mIsPaginationNeeded;
    private LinearLayoutManager mLayoutManager;
    private TrackJobsAdapter mJobAdapter;
    private ArrayList<SearchJobModel> mJobListData;

    public static ShortlistedJobsFragment newInstance() {
        return new ShortlistedJobsFragment();
    }

    public ShortlistedJobsFragment() {
        setArguments(new Bundle());
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

    private void cancelJob(final int id, String msg) {
        viewModel.cancelJob(id, msg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shortlisted_jobs, container, false);
        initViews();
        mBinding.rvFragmentShortlistedJobs.setLayoutManager(mLayoutManager);
        mBinding.rvFragmentShortlistedJobs.setAdapter(mJobAdapter);
        mBinding.rvFragmentShortlistedJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });
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
                mBinding.layJobListPagination.setVisibility(View.VISIBLE);
                mPage++;
                getAllShortListedJobs(true, false);
            }
        }
    }

    @Override
    public void onPause() {
        Bundle bundle = getArguments();
        if (bundle != null)
            bundle.putParcelableArrayList(DATA_ARRAY, mJobListData);
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelableArrayList(DATA_ARRAY) != null) {
            ArrayList<SearchJobModel> jobData = getArguments().getParcelableArrayList(DATA_ARRAY);

            if (jobData != null && jobData.size() > 0) {
                mJobListData.addAll(jobData);
            } else {
                getAllShortListedJobs(false, true);
            }

        } else {
            getAllShortListedJobs(false, true);
        }
    }

    private void initViews() {
        mJobListData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mJobAdapter = new TrackJobsAdapter(getActivity(), mJobListData, false, false, this);
        mBinding.swipeRefreshJobList.setColorSchemeResources(R.color.colorAccent);
        mBinding.swipeRefreshJobList.setOnRefreshListener(this);
        viewModel.getShortListedJobs().observe(this, this::onSuccessShortListedJobsRequest);
        viewModel.getShortListedFailed().observe(this, e -> refreshView());
        viewModel.getCancelJob().observe(this, this::onSuccessCancelJob);
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

    private void onSuccessShortListedJobsRequest(@Nullable Pair<SearchJobResponse, Boolean> result) {
        if (result != null && result.getFirst() != null && result.getSecond() != null) {
            if (!result.getSecond()) {
                mJobListData.clear();
            }
            processResponse(result.getFirst());
            mJobAdapter.notifyDataSetChanged();
            refreshView();
        }
    }

    private void getAllShortListedJobs(final boolean isPaginationLoading, boolean showProgress) {
        int type = Constants.SEARCHJOBTYPE.SHORTLISTED.getValue();
        double lat = 0, lng = 0;
        viewModel.requestAllShortListedJobs(type, mPage, lat, lng, isPaginationLoading, showProgress);
    }

    private void refreshView() {
        if (mBinding.swipeRefreshJobList.isRefreshing()) {
            mBinding.swipeRefreshJobList.setRefreshing(false);
        }
        if (mBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
            mBinding.layJobListPagination.setVisibility(View.GONE);
        }
        if (mJobListData.size() > 0) {
            mBinding.tvNoJobs.setVisibility(View.GONE);
        } else {
            mBinding.tvNoJobs.setVisibility(View.VISIBLE);
        }
    }

    private void processResponse(SearchJobResponse response) {
        if (response.getSearchJobResponseData() != null && response.getSearchJobResponseData().getList() != null) {
            mJobListData.addAll(response.getSearchJobResponseData().getList());
            int mTotalResultCount = response.getSearchJobResponseData().getTotal();
            mIsPaginationNeeded = !(mTotalResultCount == mJobListData.size());
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        getAllShortListedJobs(false, false);
    }

    @Override
    public void unSaveJob(int id, int position) {
        viewModel.unSaveJob(id, position);
    }
}
