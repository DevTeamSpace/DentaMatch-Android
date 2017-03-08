package com.appster.dentamatch.ui.notification;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityNotificationBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.network.response.notification.NotificationResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by bawenderyandra on 08/03/17.
 */

public class MyNotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private ActivityNotificationBinding mBinder;
    private MyNotificationAdapter mNotificationAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsPaginationNeeded;
    private int mPage = 1;
    private ArrayList<NotificationData> mNotificationData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        intiView();
        mBinder.rvNotification.setLayoutManager(mLayoutManager);
        mBinder.rvNotification.setAdapter(mNotificationAdapter);

        mBinder.rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });

        getNotificationsData(false, false);
    }

    private void getNotificationsData(final boolean isRefreshing, boolean isPaginating) {

        if(!isPaginating && !isRefreshing){
            processToShowDialog("", getString(R.string.please_wait), null);
        }

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);

        webServices.getNotification(mPage).enqueue(new BaseCallback<NotificationResponse>(this) {
            @Override
            public void onSuccess(NotificationResponse response) {

                if(response.getStatus() == 1){

                    if(isRefreshing){
                        mNotificationData.clear();
                    }


                    mNotificationData.addAll(response.getNotificationResponseData().getNotificationList());
                    mNotificationAdapter.notifyDataSetChanged();

                    if(response.getNotificationResponseData().getTotal() != mNotificationData.size()){
                        mIsPaginationNeeded = true;
                        mPage++;
                    }else{
                        mIsPaginationNeeded = false;
                    }

                    if(mNotificationData.size() > 0){
                        mBinder.layoutEmptyNotification.setVisibility(View.GONE);
                    }else{
                        mBinder.layoutEmptyNotification.setVisibility(View.VISIBLE);
                    }

                }else{
                    showToast(response.getMessage());
                }

                if(mBinder.swipeRefreshNotification.isRefreshing()){
                    mBinder.swipeRefreshNotification.setRefreshing(false);
                }

                if(mBinder.layPagination.getVisibility() == View.VISIBLE){
                    mBinder.layPagination.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(Call<NotificationResponse> call, BaseResponse baseResponse) {
                if(mBinder.swipeRefreshNotification.isRefreshing()){
                    mBinder.swipeRefreshNotification.setRefreshing(false);
                }

                if(mBinder.layPagination.getVisibility() == View.VISIBLE){
                    mBinder.layPagination.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkIfItsLastItem() {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (mIsPaginationNeeded) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mPage++;
                mIsPaginationNeeded = false;
                mBinder.layPagination.setVisibility(View.VISIBLE);
                getNotificationsData(false, true);
            }
        }
    }

    private void intiView() {
        mNotificationData = new ArrayList<>();
        mNotificationAdapter = new MyNotificationAdapter(this,mNotificationData);
        mBinder.toolbarNotification.tvToolbarGeneralLeft.setText(getString(R.string.header_notification));
        mBinder.swipeRefreshNotification.setColorSchemeResources(R.color.colorAccent);
        mBinder.swipeRefreshNotification.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(this);

        mBinder.toolbarNotification.ivToolBarLeft.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mNotificationData.clear();
        getNotificationsData(true, false);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }


}

