package com.appster.dentamatch.ui.tracks;

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

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.TrackJobsAdapter;
import com.appster.dentamatch.databinding.FragmentSavedJobsBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 02/02/17.
 */

public class SavedJobFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DATA_ARRAY = "DATA_ARRAY";
    private FragmentSavedJobsBinding mBinding;
    private int mPage = 1;
    private boolean mIsPaginationNeeded;
    private LinearLayoutManager mLayoutManager;
    private TrackJobsAdapter mJobAdapter;
    private ArrayList<SearchJobModel> mJobListData;
    private int mTotalResultCount;

    public static SavedJobFragment newInstance() {
        return new SavedJobFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getParcelableArrayList(DATA_ARRAY) != null) {
            ArrayList<SearchJobModel> jobData = getArguments().getParcelableArrayList(DATA_ARRAY);
            mJobListData.addAll(jobData);

        } else {
            getAllSavedJobs(false, true);
        }
    }

    @Override
    public void onPause() {
        getArguments().putParcelableArrayList(DATA_ARRAY, mJobListData);
        super.onPause();
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
                getAllSavedJobs(true, false);
            }
        }
    }

    private void getAllSavedJobs(final boolean isPaginationLoading, boolean showProgress) {

        Location userLocation = (Location) PreferenceUtil.getUserCurrentLocation();
        int type = Constants.SEARCHJOBTYPE.SAVED.getValue();
        double lat = userLocation.getLatitude();
        double lng = userLocation.getLongitude();

        if (showProgress) {
            showProgressBar(getString(R.string.please_wait));
        }

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.fetchTrackJobs(type, mPage, lat, lng).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(SearchJobResponse response) {
                showToast(response.getMessage());

                if (response.getStatus() == 1) {

                    if (!isPaginationLoading) {
                        mJobListData.clear();
                        processResponse(response);
                    } else {
                        processResponse(response);
                    }

                    mJobAdapter.notifyDataSetChanged();
                }

                if (mBinding.swipeRefreshJobList.isRefreshing()) {
                    mBinding.swipeRefreshJobList.setRefreshing(false);
                }

                if (mBinding.layJobListPagination.getVisibility() == View.VISIBLE) {
                    mBinding.layJobListPagination.setVisibility(View.GONE);
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

    private void processResponse(SearchJobResponse response) {
        if (response.getSearchJobResponseData() != null && response.getSearchJobResponseData().getList() != null) {
            mJobListData.addAll(response.getSearchJobResponseData().getList());
            /**
             * In case total item count and the job received size is equal, then pagination is not required.
             */
            mTotalResultCount = response.getSearchJobResponseData().getTotal();
            mIsPaginationNeeded = !(mTotalResultCount == mJobListData.size());
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        getAllSavedJobs(false, false);
    }
}
