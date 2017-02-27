package com.appster.dentamatch.ui.tracks;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.TrackJobsAdapter;
import com.appster.dentamatch.model.SaveUnSaveEvent;
import com.appster.dentamatch.model.TrackJobListRetrievedEvent;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Appster on 02/02/17.
 */

public class SavedJobFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DATA_ARRAY = "DATA_ARRAY";
    private com.appster.dentamatch.databinding.FragmentSavedJobsBinding mBinding;
    private LinearLayoutManager mLayoutManager;
    private TrackJobsAdapter mJobAdapter;
    private ArrayList<SearchJobModel> mJobListData;

    public static SavedJobFragment newInstance() {
        return new SavedJobFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!EventBus.getDefault().isRegistered(this)){
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_jobs, container, false);
        initViews();
        mBinding.rvFragmentSavedJobs.setLayoutManager(mLayoutManager);
        mBinding.rvFragmentSavedJobs.setAdapter(mJobAdapter);
        mBinding.rvFragmentSavedJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isActive() && isVisible()) {
            TrackJobsDataHelper.getInstance().requestData(getActivity(), Constants.SEARCHJOBTYPE.SAVED.getValue());
        }
    }

    private void initViews() {
        mJobListData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mJobAdapter = new TrackJobsAdapter(getActivity(), mJobListData, true, false, false);
        mBinding.swipeRefreshJobList.setColorSchemeResources(R.color.colorAccent);
        mBinding.swipeRefreshJobList.setOnRefreshListener(this);
    }

    public SavedJobFragment() {
        setArguments(new Bundle());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * if the recycler view has reached its last item , update data via pagination if required.
     */
    private void checkIfItsLastItem() {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (TrackJobsDataHelper.getInstance().isPaginationNeeded(Constants.SEARCHJOBTYPE.SAVED.getValue())) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mBinding.layJobListPagination.setVisibility(View.VISIBLE);
                TrackJobsDataHelper.getInstance().requestPaginatedData(getActivity(), Constants.SEARCHJOBTYPE.SAVED.getValue());

            }
        }
    }

    /**
     * Handling in case the job is unsaved and might clear the list of jobs.
     * @param event : the event that has happened.
     */
    @Subscribe
    public void unSaved(SaveUnSaveEvent event){
        if(mJobListData.size() > 0){
            mBinding.tvNoJobs.setVisibility(View.GONE);
        }else{
            mBinding.tvNoJobs.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onDataUpdated(TrackJobListRetrievedEvent event){
        if(event != null){

            if(event.getType() == Constants.SEARCHJOBTYPE.SAVED.getValue()) {
                mJobListData.clear();
                mJobListData.addAll(event.getmData());

                if(mJobListData.size() == 0){
                    mBinding.tvNoJobs.setVisibility(View.VISIBLE);
                }else{
                    mBinding.tvNoJobs.setVisibility(View.GONE);
                }

                mJobAdapter.notifyDataSetChanged();
            }

            /**
             * Hide pagination loader if it is visible.
             */
            if(mBinding.layJobListPagination.getVisibility() == View.VISIBLE){
                mBinding.layJobListPagination.setVisibility(View.GONE);
            }
            /**
             * Stop refreshing if the swipe loader is refreshing.
             */
            if(mBinding.swipeRefreshJobList.isRefreshing()){
                mBinding.swipeRefreshJobList.setRefreshing(false);
            }

        }
    }

    @Override
    public void onRefresh() {
        TrackJobsDataHelper.getInstance().refreshData(getActivity(), Constants.SEARCHJOBTYPE.SAVED.getValue());
    }
}
