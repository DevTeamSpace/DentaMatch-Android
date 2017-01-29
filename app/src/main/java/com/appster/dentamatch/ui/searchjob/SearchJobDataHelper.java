package com.appster.dentamatch.ui.searchjob;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.appster.dentamatch.model.JobDataReceivedEvent;
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
 */

class SearchJobDataHelper {
    private ProgressDialog mProgressDialog;
    private static SearchJobDataHelper ourInstance;
    private ArrayList<SearchJobModel> jobDataList;
    private boolean mIsPaginationRequired;
    private int mPageNumber;
    private int mTotalResultCount;

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
        mProgressDialog = new ProgressDialog(ct);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void searchJob(final Context ct, final boolean isPaginationLoading) {
        SearchJobRequest request = (SearchJobRequest) PreferenceUtil.getJobFilter();

        if(request != null) {
            /**
             * Do not show loader in case of pagination call.
             */
            if (!isPaginationLoading) {
                showProgress(ct);
            }
            request.setPage(mPageNumber);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
            webServices.searchJob(request).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) ct) {
                @Override
                public void onSuccess(SearchJobResponse response) {
                    hideProgress();
                    if (response.getStatus() == 1) {

                        if (!isPaginationLoading) {
                            jobDataList.clear();
                            processResponse(response);
                        } else {
                            processResponse(response);
                        }

                    } else {
                        Toast.makeText(ct, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


                @Override
                public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {
                    hideProgress();

                }
            });
        }
    }

    private void processResponse(SearchJobResponse response) {
        if (response.getSearchJobResponseData() != null && response.getSearchJobResponseData().getList() != null) {
            jobDataList.addAll(response.getSearchJobResponseData().getList());
            /**
             * In case total item count and the job received size is equal, then pagination is not required.
             */
            mTotalResultCount = response.getSearchJobResponseData().getTotal();
            mIsPaginationRequired = !(mTotalResultCount == jobDataList.size());
            EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount));
        }
    }

    public void requestData(Context ct) {
        if (jobDataList.size() > 0) {
            EventBus.getDefault().post(new JobDataReceivedEvent(jobDataList, mIsPaginationRequired, mTotalResultCount));
        } else {
            searchJob(ct, false);
        }
    }

    public void updateDataViaPagination(Context ct) {
        mPageNumber++;
        searchJob(ct, true);
    }

    public void requestRefreshData(Context ct){
        jobDataList.clear();
        mPageNumber = 1;
        searchJob(ct, true);
    }
}
