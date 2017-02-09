package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemMessageListBinding;
import com.appster.dentamatch.model.ChatHistoryModel;

import java.util.ArrayList;

/**
 * Created by Appster on 07/02/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyHolder> implements View.OnClickListener {
    private ItemMessageListBinding mBinding;
    private ArrayList<ChatHistoryModel> mData;
    private Context mContext;

    public MessageListAdapter(Context ct, ArrayList<ChatHistoryModel> data){
        mData = data;
        mContext = ct;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_list, parent, false);
        return new  MyHolder(mBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(mData.get(position) != null){
            ChatHistoryModel dataModel = mData.get(position);
            holder.tvRecruiterName.setText(dataModel.getName());
            holder.tvLastMessage.setText(dataModel.getMessage());
            holder.tvDate.setText(dataModel.getTimestamp());
            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null ){
            return mData.size();
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, ChatActivity.class));
    }

    class MyHolder extends RecyclerView.ViewHolder {
         TextView tvRecruiterName, tvLastMessage, tvDate;

          MyHolder(View itemView) {
             super(itemView);
             tvRecruiterName = mBinding.tvMessageRecruiterName;
             tvLastMessage = mBinding.tvMessageChat;
             tvDate = mBinding.tvMessageDate;
         }
     }
}
