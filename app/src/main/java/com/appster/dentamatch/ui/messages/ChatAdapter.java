package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemChatMesssageBinding;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;

/**
 * Created by Appster on 08/02/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {
    private ItemChatMesssageBinding mBinding;
    private ArrayList<Message> mChatMessages;
    private Context mContext;
    private String previousDateLabel = "";

    public ChatAdapter(Context ct) {
        mContext = ct;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_messsage, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (mChatMessages.get(position) != null) {
            holder.tvMessage.setText(mChatMessages.get(position).getMessage());


            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            timeParams.setMargins(10, 0, 10, 10);

            if (mChatMessages.get(position).getType() == Message.TYPE_MESSAGE_RECEIVED) {
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.chat_text_color_received));
                holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_chat_bubble_recieved));
                holder.tvMessage.setLayoutParams(params);

                timeParams.addRule(RelativeLayout.ALIGN_START, holder.tvMessage.getId());
                timeParams.addRule(RelativeLayout.BELOW, holder.tvMessage.getId());
                holder.tvTime.setLayoutParams(timeParams);

            } else {
                holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_chat_bubble_sent));
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
                holder.tvMessage.setLayoutParams(params);
                timeParams.addRule(RelativeLayout.ALIGN_END, holder.tvMessage.getId());
                timeParams.addRule(RelativeLayout.BELOW, holder.tvMessage.getId());
                holder.tvTime.setLayoutParams(timeParams);
            }

            holder.tvTime.setText(Utils.convertUTCtoLocalFromTimeStamp(mChatMessages.get(position).getmMessageTime()));

            /**
             * Update date label for chat messages.
             */
            if (TextUtils.isEmpty(previousDateLabel)) {
                previousDateLabel = Utils.compareDateForDateLabel(mChatMessages.get(position).getmMessageTime());
                holder.tvDateLabel.setVisibility(View.VISIBLE);
                holder.tvDateLabel.setText(previousDateLabel);
            }else {
                if(previousDateLabel.equalsIgnoreCase(Utils.compareDateForDateLabel(mChatMessages.get(position).getmMessageTime()))){
                    holder.tvDateLabel.setVisibility(View.GONE);
                }else{
                    previousDateLabel = Utils.compareDateForDateLabel(mChatMessages.get(position).getmMessageTime());
                    holder.tvDateLabel.setVisibility(View.VISIBLE);
                    holder.tvDateLabel.setText(previousDateLabel);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mChatMessages != null) {
            return mChatMessages.size();
        } else {
            return 0;
        }

    }

    void addMessage(Message message) {

        if (mChatMessages == null) {
            mChatMessages = new ArrayList<>();
        }

        mChatMessages.add(message);
        notifyItemInserted(mChatMessages.size() - 1);

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
