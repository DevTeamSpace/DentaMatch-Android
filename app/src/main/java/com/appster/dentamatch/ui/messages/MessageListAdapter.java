package com.appster.dentamatch.ui.messages;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemMessageListBinding;

/**
 * Created by Appster on 07/02/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyHolder> {
    private ItemMessageListBinding mBinding;

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_list, parent, false);
        return new  MyHolder(mBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
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
