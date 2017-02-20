package com.appster.dentamatch.ui.notification;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.LinearLayoutManager;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        intiView();
        getNotification(1);
    }

    private void intiView() {
        mBinder.toolbarNotification.tvToolbarGeneralLeft.setText(getString(R.string.header_notification));
        mLayoutManager = new LinearLayoutManager(this);
        mBinder.rvNotification.setLayoutManager(mLayoutManager);
//        mBinder.rvNotification.addItemDecoration(new SimpleDividerItemDecoration(this));
        mBinder.toolbarNotification.ivToolBarLeft.setOnClickListener(this);
        mNotificaionAdapter = new NotificaionAdapter(this, this);
        mBinder.rvNotification.setAdapter(mNotificaionAdapter);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void getNotification(int id) {
        processToShowDialog("", getString(R.string.please_wait), null);

        ReadNotificationRequest request = new ReadNotificationRequest();
        request.setNotificationId(id);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.readNotification(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                /**
                 * Once data has been loaded from the filter changes we can dismiss this filter.
                 */
                if (response.getStatus() == 1) {

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
        readNotification(position, notifId, notificationType);
    }

    private void readNotification(final int position, int notificationId, int NotificationType) {
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getNotification(notificationId).enqueue(new BaseCallback<NotificationResponse>(this) {
            @Override
            public void onSuccess(NotificationResponse response) {
                /**
                 * Once data has been loaded from the filter changes we can dismiss this filter.
                 */
                if (response.getStatus() == 1) {
                    ArrayList<NotificationData> list = response.getNotificationResponseData().getNotificationList();
                    list.get(position).setSeen(1);
                    mNotificaionAdapter.resetJobList(list);


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
