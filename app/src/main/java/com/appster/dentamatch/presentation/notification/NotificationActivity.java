/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.notification;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityNotificationBinding;
import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.network.response.notification.NotificationResponse;
import com.appster.dentamatch.util.SwipeToDeleteCallback;
import com.appster.dentamatch.presentation.searchjob.ApplyTempJobCallback;
import com.appster.dentamatch.presentation.searchjob.ApplyTempJobFragment;
import com.appster.dentamatch.util.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Pair;


public class NotificationActivity extends BaseLoadingActivity<NotificationViewModel>
        implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        NotificationAdapter.NotificationAdapterCallback, Toolbar.OnMenuItemClickListener,
{, ApplyTempJobCallback

    private ActivityNotificationBinding mBinder;
    private NotificationAdapter mNotificationAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsPaginationNeeded;
    private int mPage = 1;
    private ArrayList<NotificationData> mNotificationData;
    @Nullable
    private Snackbar mUndoSnackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        intiView();
        mBinder.rvNotification.setLayoutManager(mLayoutManager);
        mBinder.rvNotification.setAdapter(mNotificationAdapter);

        mBinder.rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });

        getNotificationsData(false, false);

        viewModel.getNotificationResponse().observe(this, this::onSuccessRequestNotifications);
        viewModel.getNotificationFailed().observe(this, throwable -> onFailedNotificationRequest());
        viewModel.getAcceptNotification().observe(this, id -> mNotificationAdapter.onAcceptNotification(id));
        viewModel.getReadNotification().observe(this, id -> mNotificationAdapter.onReadNotification(id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void onFailedNotificationRequest() {
        if (mBinder.swipeRefreshNotification.isRefreshing()) {
            mBinder.swipeRefreshNotification.setRefreshing(false);
        }
        if (mBinder.layPagination.getVisibility() == View.VISIBLE) {
            mBinder.layPagination.setVisibility(View.GONE);
        }
    }

    private void onSuccessRequestNotifications(@Nullable Pair<NotificationResponse, Boolean> result) {
        if (result != null && result.getFirst() != null && result.getSecond() != null) {
            NotificationResponse response = result.getFirst();
            boolean isRefreshing = Boolean.TRUE.equals(result.getSecond());
            if (isRefreshing) {
                mNotificationData.clear();
            }
            mNotificationData.addAll(response.getNotificationResponseData().getNotificationList());
            mNotificationAdapter.notifyDataSetChanged();
            mIsPaginationNeeded = response.getNotificationResponseData().getTotal() != mNotificationData.size();
            if (mNotificationData.size() > 0) {
                showHideEmptyLabel(View.GONE);
            } else {
                showHideEmptyLabel(View.VISIBLE);
            }
            if (mBinder.swipeRefreshNotification.isRefreshing()) {
                mBinder.swipeRefreshNotification.setRefreshing(false);
            }
            if (mBinder.layPagination.getVisibility() == View.VISIBLE) {
                mBinder.layPagination.setVisibility(View.GONE);
            }
        }
    }

    public void showHideEmptyLabel(int visibility) {
        mBinder.layoutEmptyNotification.setVisibility(visibility);
    }

    private void getNotificationsData(final boolean isRefreshing, boolean isPaginating) {
        boolean showRefreshing = !isPaginating && !isRefreshing;
        viewModel.requestNotifications(mPage, showRefreshing);
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
        mNotificationAdapter = new NotificationAdapter(this, mNotificationData, this);
        new ItemTouchHelper(new SwipeToDeleteCallback(mNotificationAdapter))
                .attachToRecyclerView(mBinder.rvNotification);
        mBinder.toolbarNotification.tvToolbarGeneralLeft.setText(getString(R.string.header_notification));
        mBinder.swipeRefreshNotification.setColorSchemeResources(R.color.colorAccent);
        mBinder.swipeRefreshNotification.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(this);

        mBinder.toolbarNotification.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarNotification.toolbarGeneral.getMenu().clear();
        mBinder.toolbarNotification.toolbarGeneral.inflateMenu(R.menu.notifications_list_menu);
        mBinder.toolbarNotification.toolbarGeneral.setOnMenuItemClickListener(this);
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

    @Override
    public void readNotification(int id) {
        viewModel.readNotification(id);
    }

    @Override
    public void acceptNotification(int notificationId, @NonNull JobDetailModel jobDetailModel) {
        ActivityUtils.Companion.replaceFragment(getSupportFragmentManager(),
                android.R.id.content,
                ApplyTempJobFragment.Companion.newInstance(notificationId, jobDetailModel),
                ApplyTempJobFragment.TAG,
                true);
    }

    @Override
    public void rejectNotification(int notificationId) {
        viewModel.rejectNotification(notificationId);
    }

    @Override
    public void deleteNotification(@NonNull ArrayList<Integer> iDs) {
        showUndoSnackbar(iDs);
    }

    private void showUndoSnackbar(@NonNull ArrayList<Integer> notificationIDs) {
        if (mUndoSnackbar != null) {
            mUndoSnackbar.dismiss();
        }
        mUndoSnackbar = Snackbar.make(findViewById(android.R.id.content),
                R.string.notification_snack_bar,
                Snackbar.LENGTH_LONG);
        mUndoSnackbar.setAction(R.string.notification_snack_bar_undo, v -> undoDelete());
        mUndoSnackbar.show();
        mUndoSnackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    viewModel.deleteNotifications(notificationIDs);
                }
            }
        });
    }

    private void undoDelete() {
        mNotificationAdapter.revert();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.notifications_remove) {
            mNotificationAdapter.deleteAll();
            return true;
        }
        return false;
    }

    @Override
    public void onDatesSelected(int notificationId, @NotNull ArrayList<String> dates) {
        viewModel.acceptNotification(notificationId, dates);
    }
}

