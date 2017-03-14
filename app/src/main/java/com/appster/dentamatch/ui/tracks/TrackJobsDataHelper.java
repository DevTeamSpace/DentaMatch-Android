package com.appster.dentamatch.ui.tracks;


import android.content.Context;
import android.location.Location;

import com.appster.dentamatch.R;
import com.appster.dentamatch.EventBus.TrackJobListRetrievedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 22/02/17.
 */

public class TrackJobsDataHelper {
    private static TrackJobsDataHelper mInstance;
    private int mSearchPageNum;
    private int mAppliedPageNum;
    private int mShortListedPageNum;

    private int mSearchTotal;
    private int mAppliedTotal;
    private int mShortListedTotal;

    private boolean mSavedPaginationNeeded;
    private boolean mAppliedPaginationNeeded;
    private ArrayList<SearchJobModel> mSavedJobData;
    private ArrayList<SearchJobModel> mAppliedJobData;

    public static TrackJobsDataHelper getInstance() {
        if (mInstance == null) {
            synchronized (TrackJobsDataHelper.class) {
                if (mInstance == null) {
                    mInstance = new TrackJobsDataHelper();
                }
            }
        }

        return mInstance;
    }

    private TrackJobsDataHelper() {
        mSavedJobData = new ArrayList<>();
        mAppliedJobData = new ArrayList<>();
        mSearchPageNum = 1;
        mAppliedPageNum = 1;
    }

    public void requestData(Context ct, int TrackJobType) {
        if (TrackJobType == Constants.SEARCHJOBTYPE.SAVED.getValue()) {

            if (mSavedJobData.size() == 0) {
                getAllSavedJobs(false, ct);
            } else {
                EventBus.getDefault().post(new TrackJobListRetrievedEvent(mSavedJobData, TrackJobType));
            }

        } else if (TrackJobType == Constants.SEARCHJOBTYPE.APPLIED.getValue()) {
            if (mAppliedJobData.size() == 0) {
                getAllAppliedJobs(false, ct);
            } else {
                EventBus.getDefault().post(new TrackJobListRetrievedEvent(mAppliedJobData, TrackJobType));
            }
        } else {
            //TODO : Call shortlisted api
        }


    }

    public void requestPaginatedData(Context ct, int TrackJobType) {
        ArrayList<SearchJobModel> updatedData = new ArrayList<>();

        if (TrackJobType == Constants.SEARCHJOBTYPE.SAVED.getValue()) {
            mSearchPageNum++;
            mSavedPaginationNeeded = false;
            getAllSavedJobs(true, ct);

        }else if(TrackJobType == Constants.SEARCHJOBTYPE.APPLIED.getValue()){
            mAppliedPageNum++;
            mAppliedPaginationNeeded = false;
            getAllAppliedJobs(true, ct);
        }

    }

    public void refreshData(Context ct, int TrackJobType) {
        if (TrackJobType == Constants.SEARCHJOBTYPE.SAVED.getValue()) {
            mSearchPageNum = 1;
            mSavedJobData.clear();
            getAllSavedJobs(true, ct);

        }else if(TrackJobType == Constants.SEARCHJOBTYPE.APPLIED.getValue()) {
            mSearchPageNum = 1;
            mAppliedJobData.clear();
            getAllAppliedJobs(true, ct);
        }
    }

    public boolean isPaginationNeeded(int type) {
        if (type == Constants.SEARCHJOBTYPE.SAVED.getValue()) {
            return mSavedPaginationNeeded;
        } else if(type == Constants.SEARCHJOBTYPE.APPLIED.getValue()){
            return mAppliedPaginationNeeded;
        }else{
            return false;
        }
    }

    public void updateSavedData() {
        mSavedPaginationNeeded = false;
        mSearchPageNum = 1;
        mSavedJobData.clear();
    }

    /**
     * Clear all data in case the applied jobs data needs refreshing from the server.
     */
    public void updateAppliedData() {
        mAppliedPageNum = 1;
        mAppliedPaginationNeeded = false;
        mAppliedJobData.clear();
    }

    private void getAllSavedJobs(final boolean isPaginationLoading, final Context ct) {

        Location userLocation = (Location) PreferenceUtil.getUserCurrentLocation();
        int type = Constants.SEARCHJOBTYPE.SAVED.getValue();
        double lat = userLocation.getLatitude();
        double lng = userLocation.getLongitude();

        if (!isPaginationLoading) {
            ((BaseActivity) ct).showProgressBar(ct.getString(R.string.please_wait));
        }

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.fetchTrackJobs(type, mSearchPageNum, lat, lng).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) ct) {
            @Override
            public void onSuccess(SearchJobResponse response) {
                if (response.getStatus() == 1) {
                    if (response.getSearchJobResponseData().getList() != null && response.getSearchJobResponseData().getList().size() > 0) {
                        mSavedJobData.addAll(response.getSearchJobResponseData().getList());
                    }

                    mSearchTotal = response.getSearchJobResponseData().getTotal();
                    mSavedPaginationNeeded = !(mSearchTotal == mSavedJobData.size());

                } else {
                    ((BaseActivity) ct).showToast(response.getMessage());
                }

                /**
                 * Notify activity for data changes.
                 */
                EventBus.getDefault().post(new TrackJobListRetrievedEvent(mSavedJobData, Constants.SEARCHJOBTYPE.SAVED.getValue()));

            }

            @Override
            public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {

            }
        });

    }

    private void getAllAppliedJobs(final boolean isPaginationLoading, final Context ct){

        Location userLocation = (Location) PreferenceUtil.getUserCurrentLocation();
        int type = Constants.SEARCHJOBTYPE.APPLIED.getValue();
        double lat = userLocation.getLatitude();
        double lng = userLocation.getLongitude();

        if (!isPaginationLoading) {
            ((BaseActivity) ct).showProgressBar(ct.getString(R.string.please_wait));
        }

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.fetchTrackJobs(type, mAppliedPageNum, lat, lng).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) ct) {
            @Override
            public void onSuccess(SearchJobResponse response) {
                if (response.getStatus() == 1) {
                    if (response.getSearchJobResponseData().getList() != null && response.getSearchJobResponseData().getList().size() > 0) {
                        mAppliedJobData.addAll(response.getSearchJobResponseData().getList());
                    }

                    mAppliedTotal = response.getSearchJobResponseData().getTotal();
                    mAppliedPaginationNeeded = !(mAppliedTotal == mAppliedJobData.size());

                } else {
                    ((BaseActivity) ct).showToast(response.getMessage());
                }

                /**
                 * Notify activity for data changes.
                 */
                EventBus.getDefault().post(new TrackJobListRetrievedEvent(mAppliedJobData, Constants.SEARCHJOBTYPE.APPLIED.getValue()));


            }

            @Override
            public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {

            }
        });
    }



}
