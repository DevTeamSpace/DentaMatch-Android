/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.notification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemNotificationBinding;
import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.presentation.searchjob.JobDetailActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.ChatUtilsKt;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.SwipeableAdapter;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder>
        implements View.OnClickListener, View.OnLongClickListener, SwipeableAdapter {

    private final int NOTIFICATION_UNREAD = 0;

    private ItemNotificationBinding mBinding;
    private ArrayList<NotificationData> mNotificationList;
    private final Context mContext;

    @NonNull
    private final NotificationAdapterCallback mCallback;
    @Nullable
    private ArrayList<NotificationData> mRecentlyDeletedItems;

    @Override
    public void delete(int position) {
        ArrayList<NotificationData> newList = new ArrayList<>(mNotificationList);
        mRecentlyDeletedItems = mNotificationList;
        newList.remove(position);
        updateItems(newList);
        if (mRecentlyDeletedItems != null) {
            ArrayList<Integer> listToDelete = new ArrayList<>();
            listToDelete.add(mRecentlyDeletedItems.get(position).getId());
            mCallback.deleteNotification(listToDelete);
        }
    }

    private void updateItems(@NonNull ArrayList<NotificationData> newList) {
        NotificationDiffCallback diffCallback = new NotificationDiffCallback(mNotificationList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        mNotificationList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    @NotNull
    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void revert() {
        if (mRecentlyDeletedItems != null) {
            updateItems(mRecentlyDeletedItems);
        }
    }

    ArrayList<NotificationData> getItems() {
        return mNotificationList;
    }

    @Override
    public void deleteAll() {
        mRecentlyDeletedItems = mNotificationList;
        updateItems(new ArrayList<>());
        if (mRecentlyDeletedItems != null) {
            ArrayList<Integer> listToDelete = new ArrayList<>();
            for (NotificationData item : mRecentlyDeletedItems) {
                listToDelete.add(item.getId());
            }
            mCallback.deleteNotification(listToDelete);
        }
    }

    public interface NotificationAdapterCallback {
        void readNotification(int id);
        void acceptRejectNotification(int id, int status);
        void deleteNotification(ArrayList<Integer> iDs);
    }

    NotificationAdapter(Context ct,
                        ArrayList<NotificationData> notificationData,
                        @NonNull NotificationAdapterCallback callback) {
        mNotificationList = notificationData;
        mContext = ct;
        mCallback = callback;
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_notification,
                parent,
                false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NotNull MyHolder holder, int position) {
        NotificationData data = mNotificationList.get(position);
        if (data != null) {

            holder.itemView.setTag(position);
            holder.tvReject.setTag(position);
            holder.tvAccept.setTag(position);

            holder.tvDesc.setText(Utils.parseDateForNtfLst(data, data.getCurrentAvailability()));

            /*
              Change visibility of cell items based on read or unread notification status.
             */
            if (data.getSeen() == NOTIFICATION_UNREAD) {
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

            /*
              Set time of the notification.
             */
            if (data.getCreatedAt() != null) {
                holder.tvDuration.setVisibility(View.VISIBLE);
                Date date = Utils.getDateNotification(data.getCreatedAt(), Constants.DateFormat.YYYYMMDDHHMMSS);
                if (date != null) {
                    holder.tvDuration.setText(Utils.getDuration(date, mContext));
                }
            } else {
                holder.tvDuration.setVisibility(View.GONE);
            }


            /*
              if notification is OTHER we hide jobType, address,accept - reject layout and right arrow.
              if notification is INVITE type show all views as visible.
              else accept - reject layout is hidden and all other views are visible
             */
            if (data.getNotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_OTHER || data.getNotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_LICENCE_ACCEPT_REJ) {
                holder.layoutInVite.setVisibility(View.GONE);
                holder.tvJobType.setVisibility(View.GONE);
                holder.tvAddress.setVisibility(View.GONE);
                holder.ivRightArrow.setVisibility(View.GONE);

            } else if (data.getNotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
                holder.layoutInVite.setVisibility(View.VISIBLE);
                holder.tvJobType.setVisibility(View.VISIBLE);
                holder.ivRightArrow.setVisibility(View.VISIBLE);
                setJobDetailData(data.getJobDetailModel(), holder);
                holder.tvAddress.setVisibility(View.GONE);
            } else {
                holder.layoutInVite.setVisibility(View.GONE);
                holder.tvJobType.setVisibility(View.VISIBLE);
                holder.tvAddress.setVisibility(View.VISIBLE);
                holder.ivRightArrow.setVisibility(View.VISIBLE);
                setJobDetailData(data.getJobDetailModel(), holder);

            }

            if (data.getNotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE && data.getSeen() == NOTIFICATION_UNREAD) {
                holder.layoutInVite.setVisibility(View.VISIBLE);

            } else {
                holder.layoutInVite.setVisibility(View.GONE);
            }
            int senderId = data.getSenderId();
            holder.itemView.setOnClickListener(v -> {
                JobDetailModel jobDetailModel = data.getJobDetailModel();
                if (jobDetailModel != null) {
                    int notificationID = jobDetailModel.getId();
                    if (data.getNotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
                        redirectToDetail(notificationID, senderId);
                    } else if (data.getSeen() == NOTIFICATION_UNREAD) {
                        mCallback.readNotification(getIdByPosition(position));
                    } else {
                            /*
                              In case of recruiter deleted the job then user doesn't get the
                              jobDetailModel.
                             */
                            redirectToDetail(notificationID);
                    }
                }
            });
            holder.messageButton.setOnClickListener(v ->
                    ChatUtilsKt.startChatWithUser(mContext, String.valueOf(senderId)));
        } else {
            holder.tvAddress.setVisibility(View.GONE);
            holder.tvJobType.setVisibility(View.GONE);
        }
        holder.tvReject.setOnClickListener(this);
        holder.tvAccept.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    private void setJobDetailData(JobDetailModel model, MyHolder holder) {
        if (model != null) {

            if (!TextUtils.isEmpty(model.getAddress())) {
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvAddress.setText(model.getAddress());
            } else {
                holder.tvAddress.setVisibility(View.GONE);
            }

            if (model.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                holder.tvJobType.setText(mContext.getString(R.string.txt_part_time));
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

            } else if (model.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                holder.tvJobType.setText(mContext.getString(R.string.txt_temporary));
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);


            } else {
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                holder.tvJobType.setText(mContext.getString(R.string.txt_full_time));

            }
        }
    }

    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.tv_reject:
                Alert.createYesNoAlert(mContext,
                        mContext.getString(R.string.txt_ok),
                        mContext.getString(R.string.txt_cancel),
                        mContext.getString(R.string.txt_alert_title),
                        mContext.getString(R.string.msg_invite_reject_warning),
                        new Alert.OnAlertClickListener() {

                            @Override
                            public void onPositive(DialogInterface dialog) {
                                mCallback.acceptRejectNotification(getIdByPosition(position), 0);
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.tv_accept:
                mCallback.acceptRejectNotification(getIdByPosition(position), 1);
                break;
        }
    }

    private void redirectToDetail(int notificationID) {
        mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                .putExtra(Constants.EXTRA_JOB_DETAIL_ID, notificationID));
    }

    private void redirectToDetail(int notificationID, int recruiterId) {
        mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                .putExtra(Constants.EXTRA_JOB_DETAIL_ID, notificationID)
                .putExtra(Constants.EXTRA_RECRUITER_ID, recruiterId));
    }

    private int getIdByPosition(int position) {
        return mNotificationList.get(position).getId();
    }

    void onAcceptNotification(@Nullable Integer id) {
        int position = getPositionById(id);
        if (position != RecyclerView.NO_POSITION) {
            if (mNotificationList.get(position) != null) {
                mNotificationList.get(position).setSeen(1);
                updateItems(mNotificationList);
            }
        }
    }

    void onDeleteNotification(@Nullable Integer id) {
        int position = getPositionById(id);
        if (position != RecyclerView.NO_POSITION) {
            mNotificationList.remove(position);updateItems(mNotificationList);
            updateItems(mNotificationList);
            if (mNotificationList.size() == 0) {
                ((NotificationActivity) mContext).showHideEmptyLabel(View.VISIBLE);
            } else {
                ((NotificationActivity) mContext).showHideEmptyLabel(View.GONE);
            }
        }
    }

    private int getPositionById(@Nullable Integer id) {
        if (id != null) {
            for (int i = 0; i < mNotificationList.size(); i++) {
                NotificationData notification = mNotificationList.get(i);
                if (notification.getId() == id) {
                    return i;
                }
            }
        }
        return RecyclerView.NO_POSITION;
    }

    void onReadNotification(@Nullable Integer id) {
        int position = getPositionById(id);
        if (position != RecyclerView.NO_POSITION) {
            NotificationData data = mNotificationList.get(position);
            int NOTIFICATION_READ = 1;
            data.setSeen(NOTIFICATION_READ);
            updateItems(mNotificationList);
            if (data.getJobDetailModel() != null) {
                redirectToDetail(data.getJobDetailModel().getId());
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        Alert.createYesNoAlert(mContext, mContext.getString(R.string.txt_ok),
                mContext.getString(R.string.txt_cancel),
                mContext.getString(R.string.txt_alert_title),
                mContext.getString(R.string.msg_warning_delete_notification),
                new Alert.OnAlertClickListener() {

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        ArrayList<Integer> itemsToDelete = new ArrayList<>();
                        itemsToDelete.add(position);
                        mCallback.deleteNotification(itemsToDelete);
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
        return false;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private final CustomTextView tvDesc;
        private final CustomTextView tvAddress;
        private final CustomTextView tvDuration;
        private final CustomTextView tvJobType;
        private final Button tvAccept;
        private final Button tvReject;
        private final Button messageButton;
        private final ImageView ivRead;
        private final ImageView ivRightArrow;
        private final LinearLayout layoutInVite;

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
            messageButton = mBinding.notificationMessageButton;
        }
    }
}