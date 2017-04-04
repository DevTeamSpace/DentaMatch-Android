package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemChatMessageBinding;
import com.appster.dentamatch.util.Utils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Appster on 08/02/17.
 */

public class ChatAdapter extends RealmRecyclerViewAdapter<Message, ChatAdapter.MyHolder> {
    private ItemChatMessageBinding mBinding;
    private OrderedRealmCollection<Message> mChatMessages;
    private Context mContext;

    public ChatAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Message> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        mContext = context;
        mChatMessages = data;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_message, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (mChatMessages.get(position) != null) {
            holder.tvMessage.setText(mChatMessages.get(position).getMessage());


            RelativeLayout.LayoutParams receivedParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            receivedParams.setMargins(20, 20, 200, 20);

            RelativeLayout.LayoutParams sentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            sentParams.setMargins(200, 20, 20, 20);

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            timeParams.setMargins(10, 0, 10, 10);

            if (mChatMessages.get(position).getType() == Message.TYPE_MESSAGE_RECEIVED) {
                holder.tvDateLabel.setVisibility(View.GONE);
                holder.tvMessage.setVisibility(View.VISIBLE);
                holder.tvTime.setVisibility(View.VISIBLE);
                receivedParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                receivedParams.addRule(RelativeLayout.BELOW, holder.tvDateLabel.getId());
                holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.chat_text_color_received));
                holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_chat_bubble_recieved));
                holder.tvMessage.setLayoutParams(receivedParams);

                timeParams.addRule(RelativeLayout.ALIGN_START, holder.tvMessage.getId());
                timeParams.addRule(RelativeLayout.BELOW, holder.tvMessage.getId());
                holder.tvTime.setLayoutParams(timeParams);

            } else if (mChatMessages.get(position).getType() == Message.TYPE_MESSAGE_SEND) {
                holder.tvDateLabel.setVisibility(View.GONE);
                holder.tvMessage.setVisibility(View.VISIBLE);
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_chat_bubble_sent));
                sentParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                sentParams.addRule(RelativeLayout.BELOW, holder.tvDateLabel.getId());

                holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
                holder.tvMessage.setLayoutParams(sentParams);
                timeParams.addRule(RelativeLayout.ALIGN_END, holder.tvMessage.getId());
                timeParams.addRule(RelativeLayout.BELOW, holder.tvMessage.getId());
                holder.tvTime.setLayoutParams(timeParams);

            } else {
                /**
                 * Update date label for chat messages.
                 */
                holder.tvDateLabel.setVisibility(View.VISIBLE);
                holder.tvMessage.setVisibility(View.GONE);
                holder.tvTime.setVisibility(View.GONE);
                holder.tvDateLabel.setText(Utils.compareDateForDateLabel(mChatMessages.get(position).getmMessageTime()));
            }

            holder.tvTime.setText(Utils.convertUTCtoLocalFromTimeStamp(mChatMessages.get(position).getmMessageTime()));


        }
    }

    @Override
    public int getItemCount() {
        if (mChatMessages != null && mChatMessages.isValid()) {
            return mChatMessages.size();
        } else {
            return 0;
        }

    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvDateLabel;

        MyHolder(View itemView) {
            super(itemView);
            tvMessage = mBinding.tvChatMessage;
            tvTime = mBinding.tvChatTime;
            tvDateLabel = mBinding.tvChatDateLabel;
        }
    }

}
