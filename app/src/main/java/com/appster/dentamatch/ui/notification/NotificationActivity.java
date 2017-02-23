package com.appster.dentamatch.ui.notification;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityNotificationBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.Notification.ReadNotificationRequest;
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse;
import com.appster.dentamatch.network.response.jobs.HiredJobResponse;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.network.response.notification.NotificationResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.calendar.HiredJobAdapter;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.ui.searchjob.SearchJobDataHelper;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 14/02/17.
 */
public class NotificationActivity extends BaseActivity implements View.OnClickListener, NotificaionAdapter.NotificationClickListener {
    private final String TAG = "NotificationActivity";
    private ActivityNotificationBinding mBinder;
    private NotificaionAdapter mNotificaionAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsPaginationNeeded;
    private int page=1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        intiView();
        getNotification(1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.LOGD(TAG, "new Intent run");
    }

    private void intiView() {
        mBinder.toolbarNotification.tvToolbarGeneralLeft.setText(getString(R.string.header_notification));
        mLayoutManager = new LinearLayoutManager(this);
        mBinder.rvNotification.setLayoutManager(mLayoutManager);
//        mBinder.rvNotification.addItemDecoration(new SimpleDividerItemDecoration(this));
        mBinder.toolbarNotification.ivToolBarLeft.setOnClickListener(this);
        mNotificaionAdapter = new NotificaionAdapter(this, this);
        mBinder.rvNotification.setAdapter(mNotificaionAdapter);
        mBinder.rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkIfItsLastItem();
            }
        });
    }
    private void checkIfItsLastItem() {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (mIsPaginationNeeded) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                ++page;
                mIsPaginationNeeded = false;
                mBinder.layPagination.setVisibility(View.VISIBLE);
                getNotification(page);
//                SearchJobDataHelper.getInstance().updateDataViaPagination(getActivity());
            }
        }
    }
    @Override
    public String getActivityName() {
        return null;
    }

    private void readNotification(final int position, int notificationId, final int notificationType) {

        processToShowDialog("", getString(R.string.please_wait), null);

        ReadNotificationRequest request = new ReadNotificationRequest();
        request.setNotificationId(notificationId);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.readNotification(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                /**
                 * Once data has been loaded from the filter changes we can dismiss this filter.
                 */


                if (response.getStatus() == 1) {
                    ArrayList<NotificationData> list = new ArrayList<NotificationData>();
                    list.addAll(mNotificaionAdapter.getList());
                    LogUtils.LOGD(TAG, "size is---" + list.size());

                    list.get(position).setSeen(1);
                    mNotificaionAdapter.resetJobList(list);
//                    list = mNotificaionAdapter.getList();
                    if (list.get(position).getJobDetailModel() != null) {
                        redirectNotification(notificationType, list.get(position).getJobDetailModel().getId());
                    } else {
                        redirectNotification(notificationType, -1);

                    }
                } else {
                    showToast(response.getMessage());
                }
            }


            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "Failed job hired");
            }
        });
    }

    private void redirectNotification(int notificationType, int jobId) {
        Intent intent = null;
        if (notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_ACCEPT_JOB || notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_CANCEL || notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_HIRED) {
            intent = new Intent(this, JobDetailActivity.class);
            intent.putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobId);

        } else if (notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_COMPLETE_PROFILE || notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_VERIFY_DOC) {
//            intent = new Intent(this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
//        else {
//            intent = new Intent(this, NotificationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        }
        if (intent != null) {
            startActivity(intent);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                finish();
                break;


        }
    }

    @Override
    public void onNotificationItemClick(int position, int notifId, int notificationType) {
        if (mNotificaionAdapter.getList().get(position).getSeen() == 1) {
            if (mNotificaionAdapter.getList().get(position).getJobDetailModel() != null) {
                redirectNotification(notificationType, mNotificaionAdapter.getList().get(position).getJobDetailModel().getId());
            } else {
                redirectNotification(notificationType, -1);

            }

        } else {
            readNotification(position, notifId, notificationType);

        }
    }

    private void getNotification(int page) {

        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getNotification(page).enqueue(new BaseCallback<NotificationResponse>(this) {
            @Override
            public void onSuccess(NotificationResponse response) {
                /**
                 */
                if (response.getStatus() == 1) {

                    mNotificaionAdapter.setJobList(response.getNotificationResponseData().getNotificationList());
//                    if (mNotificaionAdapter.getList() == null || mNotificaionAdapter.getList().size() == 0) {
//                        mBinder.
//                    } else {
//                        mBinder.rvNotification.setVisibility(View.VISIBLE);
//                    }
                } else {
                    showToast(response.getMessage());
                }

            }


            @Override
            public void onFail(Call<NotificationResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "Failed job hired");
            }
        });
    }


}
