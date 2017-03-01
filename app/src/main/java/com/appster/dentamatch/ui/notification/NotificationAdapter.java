package com.appster.dentamatch.ui.notification;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemNotificationBinding;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by virender on 14/02/17.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {
    private ItemNotificationBinding mBinding;
    private Context mContext;
    private ArrayList<NotificationData> mNotificationList = new ArrayList<>();
    private NotificationClickListener mListener;


    public NotificationAdapter(Context context, NotificationClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public ArrayList<NotificationData> getList() {
        return mNotificationList;
    }

    public void setJobList(ArrayList<NotificationData> jobList) {
//        mNotificationData.clear();
        mNotificationList.addAll(jobList);
        notifyDataSetChanged();
    }

    public void resetJobList(ArrayList<NotificationData> jobList) {
        mNotificationList.clear();
        mNotificationList.addAll(jobList);
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_notification, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        NotificationData data = mNotificationList.get(position);

        if (data != null) {
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
                    if (data.getnotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE && data.getSeen() == 0) {
                        holder.layoutInVite.setVisibility(View.VISIBLE);

                    } else {
                        holder.layoutInVite.setVisibility(View.GONE);
                    }

                } else if (data.getJobDetailModel().getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                    holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                    holder.tvJobType.setText(mContext.getString(R.string.txt_full_time));

                }
                holder.tvAddress.setText(data.getJobDetailModel().getAddress());

//
            } else {
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvJobType.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNotificationList.get((int) view.getTag()).getnotificationType() != Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
                        mListener.onNotificationItemClick((int) view.getTag(), mNotificationList.get((int) view.getTag()).getId(), mNotificationList.get((int) view.getTag()).getnotificationType());
                    } else if (mNotificationList.get((int) view.getTag()).getnotificationType() == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE && mNotificationList.get((int) view.getTag()).getSeen() == 0) {
                        mListener.onNotificationItemClick((int) view.getTag(), mNotificationList.get((int) view.getTag()).getId(), mNotificationList.get((int) view.getTag()).getnotificationType());

                    }
                }
            });
            holder.tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        mListener.onNotificationItemClick((int) view.getTag(), mNotificationList.get((int) view.getTag()).getId(), 1);
                }
            });
            holder.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onAcceptRejectClick((int) view.getTag(), mNotificationList.get((int) view.getTag()).getId(), 0);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onDelete((int) view.getTag(), mNotificationList.get((int) view.getTag()).getId(), mNotificationList.get((int) view.getTag()).getnotificationType());

                    return false;
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        if (mNotificationList != null) {
            return mNotificationList.size();
        }
        return 0;
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

    public interface NotificationClickListener {
        public void onNotificationItemClick(int position, int notifId, int notificationType);
        public void onAcceptRejectClick(int position, int notifId, int inviteStatus);
        public void onDelete(int position, int notifId, int notificationType);
    }

}