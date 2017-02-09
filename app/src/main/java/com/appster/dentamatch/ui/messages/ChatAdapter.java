package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemChatMesssageBinding;

import java.util.ArrayList;

/**
 * Created by Appster on 08/02/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {
    private ItemChatMesssageBinding mBinding;
    private ArrayList<String> mChatMessages;
    private Context mContext;

    public ChatAdapter(Context ct){
        mContext = ct;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_messsage, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(mChatMessages.get(position) != null){
            holder.tvMessage.setText(mChatMessages.get(position));
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);

        if(position % 2 == 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.chat_text_color_received));
            holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_chat_bubble_recieved));
            holder.tvMessage.setLayoutParams(params);
        }else{
            holder.tvMessage.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_chat_bubble_sent));
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.tvMessage.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
            holder.tvMessage.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        if(mChatMessages != null){
            return mChatMessages.size();
        }else{
            return 0;
        }

    }

    public void addMessage(String str){

        if(mChatMessages == null){
            mChatMessages = new ArrayList<>();
        }

        mChatMessages.add(str);
        notifyItemInserted(mChatMessages.size() - 1);

    }

     class MyHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

         MyHolder(View itemView) {
            super(itemView);
            tvMessage = mBinding.tvChatMessage;
        }
    }
}
