package com.appster.dentamatch.ui.searchjob;

import android.content.Context;
import android.widget.Toast;

import com.appster.dentamatch.eventbus.JobDataReceivedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 28/01/17.
 * To inject activity reference.
 */

public class SearchJobDataHelper {
    private static SearchJobDataHelper ourInstance;
    private final ArrayList<SearchJobModel> jobDataList;
    private boolean mIsPaginationRequired;
    private int mPageNumber;
    private int mTotalResultCount;
    private int mIsJobSeekerVerified;
    private int mProfileCompleted;



    public synchronized static SearchJobDataHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new SearchJobDataHelper();
        }

        return ourInstance;
    }

    private SearchJobDataHelper() {
        jobDataList = new ArrayList<>();
        mPageNumber = 1;
    }

    private void showProgress(Context ct) {
        ((BaseActivity)ct).processToShowDialog();
    }

    private void hideProgress(Context ct) {
        ((BaseActivity)ct).hideProgressBar();
    }

    private void searchJob(final Context ct, final boolean isPaginationLoading) {
        SearchJobRequest request = (SearchJobRequest) PreferenceUtil.getJobFilter();

        //if (request != null) {

        if (request != null) {
            request = (SearchJobRequest) PreferenceUtil.getJobFilter();
        }else{
            request= new SearchJobRequest();
        }

            /*
              Do not show loader in case of pagination call.
             */
            if (!isPaginationLoading) {
                showProgress(ct);
            }

            request.setPage(mPageNumber);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.searchJob(request).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) ct) {
                @Override
                public void onSuccess(SearchJobResponse response) {
                    /*
                      Once data has been loaded from the filter changes we can dismiss this filter.
                     */
                    PreferenceUtil.setFilterChanged(false);
                    hideProgress(ct);
                    if (response.getStatus() == 1) {

                        mIsJobSeekerVerified=response.getSearchJobResponseData().getIsJobSeekerVerified();
                        mProfileCompleted=response.getSearchJobResponseData().getProfileCompleted();


                        if (!isPaginationLoading) {
                            jobDataList.clear();
                            processResponse(response);
                        } else {
                            processResponse(response);
                        }

                    } else {
                        Toast.makeText(ct, response.getMessage(), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount,mIsJobSeekerVerified,mProfileCompleted));
                    }

                }


                @Override
                public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {
                    EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount,mIsJobSeekerVerified,mProfileCompleted));
                    hideProgress(ct);

                }
            });
        /*}else{
            EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount,mIsJobSeekerVerified,mProfileCompleted));
        }*/
    }

    private void processResponse(SearchJobResponse response) {
        if (response.getSearchJobResponseData() != null && response.getSearchJobResponseData().getList() != null) {
            jobDataList.addAll(response.getSearchJobResponseData().getList());
            /*
              In case total item count and the job received size is equal, then pagination is not required.
             */
            mIsJobSeekerVerified=response.getSearchJobResponseData().getIsJobSeekerVerified();
            mProfileCompleted=response.getSearchJobResponseData().getProfileCompleted();

            mTotalResultCount = response.getSearchJobResponseData().getTotal();
            mIsPaginationRequired = !(mTotalResultCount == jobDataList.size());
            EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount,response.getSearchJobResponseData().getIsJobSeekerVerified(),response.getSearchJobResponseData().getProfileCompleted()));
        }
    }

    public void requestData(Context ct) {
        /*
          In case the filter is changed we clear all previous data and hit API again to refresh the data.
         */
        if (PreferenceUtil.isFilterChanged() ) {
            jobDataList.clear();
            mPageNumber = 1;
            searchJob(ct, false);

        } else {
            if (jobDataList.size() > 0) {
                EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount,mIsJobSeekerVerified,mProfileCompleted));
            } else {
                searchJob(ct, false);
            }
        }

    }

    public void updateDataViaPagination(Context ct) {
        mPageNumber++;
        searchJob(ct, true);
    }

    public void requestRefreshData(Context ct) {
        jobDataList.clear();
        mPageNumber = 1;
        searchJob(ct, true);
    }

    /**
     * Notify the helper that content of a response has been changed locally and we need to request server for refreshed data.
     * Eg. save un-save of jobs.
     */
    public void notifyItemsChanged(SearchJobModel model){
        for(SearchJobModel jobModel : jobDataList){

            if(jobModel.getId() == model.getId()){
                jobModel.setIsSaved(model.getIsSaved());
                break;
            }
        }
    }

    public void clearInstance(){
        ourInstance = null;
    }
}
