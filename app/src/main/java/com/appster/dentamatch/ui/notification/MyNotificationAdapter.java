package com.appster.dentamatch.ui.notification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemNotificationBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.Notification.AcceptRejectInviteRequest;
import com.appster.dentamatch.network.request.Notification.ReadNotificationRequest;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by bawenderyandra on 08/03/17.
 */

public class MyNotificationAdapter extends RecyclerView.Adapter<MyNotificationAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private ItemNotificationBinding mBinding;
    private ArrayList<NotificationData> mNotificationList;
    private Context mContext;

    public MyNotificationAdapter(Context ct, ArrayList<NotificationData> notificationData) {
        mNotificationList = notificationData;
        mContext = ct;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_notification, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (mNotificationList.get(position) != null) {
            NotificationData data = mNotificationList.get(position);

            holder.itemView.setTag(position);
            holder.tvReject.setTag(position);
            holder.tvAccept.setTag(position);

            holder.tvDesc.setText(data.getNotificationData());

            if (data.getSeen() == 0) {
                holder.ivRead.setVisibility(View.VISIBLE);
                holder.tvDesc.setCustomFont(mContext, mContext.getString(R.string.font_medium));
                holder.tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.black_color));
                holder.tvDuration.setTextColor(ContextCompat.getColor(mContext, R.color.black_color));
                holder.tvAddress.setTextColor(ContextCompat.getColor(mContext, R.color.black_color));
            } else {
                holder.tvDesc.setCustomFont(mContext, mContext.getString(R.string.font_regular));
                holder.tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.chat_message));
                holder.tvAddress.setTextColor(ContextCompat.getColor(mContext, R.color.chat_message));
                holder.tvDuration.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey_three));
                holder.ivRead.setVisibility(View.INVISIBLE);

            }

            if (data.getCreatedAt() != null) {
                holder.tvDuration.setText(Utils.getDuration(Utils.getDate(data.getCreatedAt(), Constants.DateFormet.YYYYMMDDHHMMSS), mContext));
            }

            if (data.getJobDetailModel() != null && data.getJobDetailModel().getJobType() != 0) {
                if (data.getJobDetailModel().getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                    holder.tvJobType.setText(mContext.getString(R.string.txt_part_time));
                    holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                } else if (data.getJobDetailModel().getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                    holder.tvJobType.setText(mContext.getString(R.string.txt_temporary));
                    holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);


                } else if (data.getJobDetailModel().getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                    holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                    holder.tvJobType.setText(mContext.getString(R.string.txt_full_time));

                }

                if (data.getnotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE && data.getSeen() == 0) {
                    holder.layoutInVite.setVisibility(View.VISIBLE);

                } else {
                    holder.layoutInVite.setVisibility(View.GONE);
                }

                holder.tvAddress.setText(data.getJobDetailModel().getAddress());

            } else {
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvJobType.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(this);
            holder.tvReject.setOnClickListener(this);
            holder.tvAccept.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);


        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {

            case R.id.tv_reject:
                callInviteStatusApi(position, 0);
                break;

            case R.id.tv_accept:
                callInviteStatusApi(position, 1);
                break;

            // ItemView Clicked
            default:
                if (mNotificationList.get(position) != null) {
                    NotificationData data = mNotificationList.get(position);

                    if (data.getnotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
                        redirectToDetail(data.getJobDetailModel().getId());

                    } else if (data.getSeen() == 0) {
                        updateSeenStatus(position, false);

                    } else {
                        redirectToDetail(data.getJobDetailModel().getId());
                    }
                }
                break;
        }
    }

    private void redirectToDetail(int notificationID) {
        mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                .putExtra(Constants.EXTRA_JOB_DETAIL_ID, notificationID));
    }

    private void updateSeenStatus(final int position, final boolean isInvite) {
        ((BaseActivity) mContext).processToShowDialog("", mContext.getString(R.string.please_wait), null);
        ReadNotificationRequest request = new ReadNotificationRequest();
        request.setNotificationId(mNotificationList.get(position).getId());

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.readNotification(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    NotificationData data = mNotificationList.get(position);
                    data.setSeen(1);
                    notifyItemChanged(position);

                    if (!isInvite) {
                        redirectToDetail(data.getJobDetailModel().getId());
                    }

                } else {
                    ((BaseActivity) mContext).showToast(response.getMessage());
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void callInviteStatusApi(final int position, int inviteStatus) {

        if (mNotificationList.get(position) != null) {
            ((BaseActivity) mContext).processToShowDialog("", mContext.getString(R.string.please_wait), null);
            AcceptRejectInviteRequest request = new AcceptRejectInviteRequest();
            request.setNotificationId(mNotificationList.get(position).getId());
            request.setAcceptStatus(inviteStatus);

            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.acceptRejectNotification(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response.getStatus() == 1) {

                        if (mNotificationList.get(position) != null) {
                            mNotificationList.get(position).setSeen(1);
                            notifyItemChanged(position);
                        }

                    } else {
                        ((BaseActivity) mContext).showToast(response.getMessage());
                    }
                }


                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                    LogUtils.LOGD(TAG, "Failed job hired");
                }
            });
        }

    }

    private void callDeleteNotificationApi(final int position) {
        if (mNotificationList.get(position) != null) {
            ((BaseActivity) mContext).processToShowDialog("", mContext.getString(R.string.please_wait), null);
            ReadNotificationRequest request = new ReadNotificationRequest();
            request.setNotificationId(mNotificationList.get(position).getId());
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.readNotification(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response.getStatus() == 1) {
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mNotificationList.size());
                    } else {
                        ((BaseActivity) mContext).showToast(response.getMessage());
                    }
                }

                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                    LogUtils.LOGD(TAG, "Failed job hired");
                }
            });
        }

    }

    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        Alert.createYesNoAlert(mContext, "OK", "CANCEL", mContext.getString(R.string.app_name), "Are you sure you want to delete the notification?", new Alert.OnAlertClickListener() {

            @Override
            public void onPositive(DialogInterface dialog) {
                callDeleteNotificationApi(position);
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        return false;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvDesc, tvAddress, tvDuration, tvJobType, tvAccept, tvReject;
        private ImageView ivRead, ivRightArrow;
        private LinearLayout layoutInVite;

        MyHolder(View itemView) {
            super(itemView);
            tvDesc = mBinding.tvDesc;
            tvJobType = mBinding.tvJobType;
            tvAddress = mBinding.tvAddress;
            tvDuration = mBinding.tvTime;
            tvAccept = mBinding.tvAccept;
            tvReject = mBinding.tvReject;
            layoutInVite = mBinding.layoutInviteNotification;
            ivRead = mBinding.ivRead;
            ivRightArrow = mBinding.ivRightArrow;
        }
    }
}

