package com.appster.dentamatch.ui.tracks;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.TrackJobsAdapter;
import com.appster.dentamatch.model.JobCancelEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.tracks.CancelJobRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 02/02/17.
 */

public class AppliedJobsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private com.appster.dentamatch.databinding.FragmentAppliedJobsBinding mBinding;
    private LinearLayoutManager mLayoutManager;
    private TrackJobsAdapter mJobAdapter;
    private ArrayList<SearchJobModel> mJobListData;
    private int mPage = 1;
    private boolean mIsPaginationNeeded;
    private int mTotalResultCount;
    private static final String DATA_ARRAY = "DATA_ARRAY";

    public static AppliedJobsFragment newInstance() {
        return new AppliedJobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    public AppliedJobsFragment(){
        setArguments(new Bundle());
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

    @Subscribe
    public void jobCancelled(JobCancelEvent event){
        if(event != null){
          for(SearchJobModel model : mJobListData){


              if(model.getId() == event.getJobID()){
                  cancelJob(event.getJobID(), event.getMsg());
                  break;
              }
          }
        }

    }

    private void cancelJob(final int ID, String msg ) {
        CancelJobRequest request = new CancelJobRequest();
        request.setCancelReason(msg);
        request.setJobId(ID);

        showProgressBar(getString(R.string.please_wait));
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.cancelJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) getActivity()) {

            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();

                if (response.getStatus() == 1) {
                    mJobAdapter.cancelJob(ID);

                    if(mJobListData.size() > 0){
                        mBinding.tvNoJobs.setVisibility(View.GONE);
                    }else{
                        mBinding.tvNoJobs.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_applied_jobs, container, false);
        initViews();
        mBinding.rvFragmentAppliedJobs.setLayoutManager(mLayoutManager);
        mBinding.rvFragmentAppliedJobs.setAdapter(mJobAdapter);
        mBinding.rvFragmentAppliedJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });
        return mBinding.getRoot();

    }

    @Override
    public void onPause() {
        getArguments().putParcelableArrayList(DATA_ARRAY, mJobListData);
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getParcelableArrayList(DATA_ARRAY) != null) {
            ArrayList<SearchJobModel> jobData = getArguments().getParcelableArrayList(DATA_ARRAY);

            if(jobData.size() > 0) {
                mJobListData.addAll(jobData);
            }else{
                getAllAppliedJobs(false, true);
            }

        } else {
            getAllAppliedJobs(false, true);
        }
    }

    private void initViews() {
        mJobListData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mJobAdapter = new TrackJobsAdapter(getActivity(), mJobListData, false, true, false);
        mBinding.swipeRefreshJobList.setOnRefreshListener(this);
    }

    private void getAllAppliedJobs(final boolean isPaginationLoading, boolean showProgress){

        Location userLocation = (Location) PreferenceUtil.getUserCurrentLocation();
        int type = Constants.SEARCHJOBTYPE.APPLIED.getValue();
        double lat = userLocation.getLatitude();
        double lng = userLocation.getLongitude();

        if (showProgress) {
            showProgressBar(getString(R.string.please_wait));
        }

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.fetchTrackJobs(type, mPage, lat, lng).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(SearchJobResponse response) {

                if (response.getStatus() == 1) {

                    if (!isPaginationLoading) {
                        mJobListData.clear();
                        processResponse(response);
                    } else {
                        processResponse(response);
                    }

                    mJobAdapter.notifyDataSetChanged();
                }else{
                    showToast(response.getMessage());
                }

                if (mBinding.swipeRefreshJobList.isRefreshing()) {
                    mBinding.swipeRefreshJobList.setRefreshing(false);
                }

                if (mBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
                    mBinding.layJobListPagination.setVisibility(View.GONE);
                }

                if(mJobListData.size() > 0){
                    mBinding.tvNoJobs.setVisibility(View.GONE);
                }else{
                    mBinding.tvNoJobs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {
                if (mBinding.swipeRefreshJobList.isRefreshing()) {
                    mBinding.swipeRefreshJobList.setRefreshing(false);
                }

                if (mBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
                    mBinding.layJobListPagination.setVisibility(View.GONE);
                }
            }
        });
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
                getAllAppliedJobs(true, false);
            }
        }
    }

    private void processResponse(SearchJobResponse response) {
        if (response.getSearchJobResponseData() != null && response.getSearchJobResponseData().getList() != null) {
            mJobListData.addAll(response.getSearchJobResponseData().getList());
            /**
             * In case total item count and the job received size is equal, then pagination is not required.
             */
            mTotalResultCount = response.getSearchJobResponseData().getTotal();
            mIsPaginationNeeded = !(mTotalResultCount == mJobListData.size());
        }

        if(mJobListData.size() > 0){
            mBinding.tvNoJobs.setVisibility(View.GONE);
        }else{
            mBinding.tvNoJobs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        getAllAppliedJobs(false, false);
    }



}
