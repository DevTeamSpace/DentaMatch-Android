/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.DBModel;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ItemMessageListBinding;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Appster on 15/02/17.
 * To inject activity reference.
 */

public class MessageListAdapter extends RealmRecyclerViewAdapter<DBModel, MessageListAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private final OrderedRealmCollection<DBModel> messagesData;
    private ItemMessageListBinding mBinding;
    private final Context mContext;
    private final String userID;
    private IDeleteMessage mListener;

    public MessageListAdapter(Context context, @Nullable OrderedRealmCollection<DBModel> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        messagesData = data;
        mContext = context;
        userID = PreferenceUtil.getUserChatId();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_message_list, parent, false);
        return new MessageListAdapter.MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        if (messagesData.get(position) != null) {
            DBModel dataModel = messagesData.get(position);
            holder.tvRecruiterName.setText(dataModel.getName());
            holder.tvLastMessage.setText(dataModel.getLastMessage());

            if (!TextUtils.isEmpty(dataModel.getLastMsgTime())) {
                holder.tvDate.setText(Utils.convertUTCToTimeLabel(dataModel.getLastMsgTime()));
            } else {
                holder.tvDate.setText("");
            }

            if (dataModel.getUnReadChatCount() == 0) {
                holder.tvUnreadChatCount.setVisibility(View.GONE);
            } else if (dataModel.getUnReadChatCount() >= 100) {
                holder.tvUnreadChatCount.setVisibility(View.VISIBLE);
                holder.tvUnreadChatCount.setText(R.string.txt_max_notification_count);
            } else {
                holder.tvUnreadChatCount.setVisibility(View.VISIBLE);
                holder.tvUnreadChatCount.setText(String.valueOf(dataModel.getUnReadChatCount()));
            }

            holder.itemView.setTag(position);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        if (messagesData != null && messagesData.isValid() && messagesData.size() > 0) {
            return messagesData.size();
        }

        return 0;
    }

    public void setListener(IDeleteMessage listener){
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        mContext.startActivity(new Intent(mContext, ChatActivity.class)
                .putExtra(Constants.EXTRA_CHAT_MODEL, messagesData.get(position).getRecruiterId()));

    }


    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        if (messagesData.get(position).getSeekerHasBlocked() == 0) {
            Alert.createYesNoAlert(mContext,
                    mContext.getString(R.string.block),
                    mContext.getString(R.string.delete),
                    mContext.getString(R.string.cancel),
                    null,
                    mContext.getString(R.string.msg_block_alert),
                    new Alert.OnAlertClickEventListener() {

                        @Override
                        public void onPositive(DialogInterface dialog) {
                            blockUnBlockUser("1", messagesData.get(position).getRecruiterId(), userID);
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNeutral(DialogInterface dialog) {
                            mListener.onDelete(messagesData.get(position).getRecruiterId(),position);
                        }
                    });
        }
        return true;
    }

    private void blockUnBlockUser(final String status, final String recruiterID, String userID) {
        if (SocketManager.getInstance().isConnected()) {
            SocketManager.getInstance().blockUnblockUser(status, recruiterID, userID);
        } else {
            ((BaseActivity) mContext).showToast(mContext.getString(R.string.error_socket_connection));
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        final TextView tvRecruiterName;
        final TextView tvLastMessage;
        final TextView tvDate;
        final TextView tvUnreadChatCount;
        final View RemovableView;
        final View ConstantView;

        MyHolder(View itemView) {
            super(itemView);
            tvRecruiterName = mBinding.tvMessageRecruiterName;
            tvUnreadChatCount = mBinding.tvMessageChatCount;
            tvLastMessage = mBinding.tvMessageChat;
            tvDate = mBinding.tvMessageDate;
            RemovableView = mBinding.swipeableView;
            ConstantView = mBinding.constantView;
        }
    }

    public interface IDeleteMessage {
        void onDelete(String id, int position);
    }
}
