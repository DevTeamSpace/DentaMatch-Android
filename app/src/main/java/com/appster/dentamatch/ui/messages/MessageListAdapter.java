package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemMessageListBinding;
import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.chat.BlockUnBlockRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 07/02/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private ItemMessageListBinding mBinding;
    private ArrayList<ChatListModel> mData;
    private Context mContext;

    public MessageListAdapter(Context ct, ArrayList<ChatListModel> data){
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
            ChatListModel dataModel = mData.get(position);
            holder.tvRecruiterName.setText(dataModel.getName());
            holder.tvLastMessage.setText(dataModel.getMessage());
            holder.tvDate.setText(Utils.compareDateFromCurrentLocalTime(dataModel.getTimestamp()));
            holder.itemView.setTag(position);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
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
        int position = (int) v.getTag();

        mContext.startActivity(new Intent(mContext, ChatActivity.class)
        .putExtra(Constants.EXTRA_CHAT_MODEL, mData.get(position)));
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        final ChatListModel recruiterData = mData.get(position);
        if(recruiterData.getRecruiterBlock() == 0) {
            Alert.createYesNoAlert(mContext,
                    mContext.getString(R.string.block),
                    mContext.getString(R.string.cancel),
                    null,
                    mContext.getString(R.string.msg_block_alert),
                    new Alert.OnAlertClickListener() {
                        @Override
                        public void onPositive(DialogInterface dialog) {
                            blockUnBlockUser(1, recruiterData.getRecruiterId(), position);
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
        }
        return false;
    }

    private void blockUnBlockUser(final int status, int recruiterID, final int position){
        BlockUnBlockRequest request = new BlockUnBlockRequest();
        request.setBlockStatus(String.valueOf(status));
        request.setRecruiterId(String.valueOf(recruiterID));

        ((BaseActivity)mContext). processToShowDialog("", mContext.getString(R.string.please_wait), null);
        AuthWebServices client = RequestController.createService(AuthWebServices.class);
        client.blockUnBlockUser(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity)mContext).showToast(response.getMessage());

                if(response.getStatus() == 1){
                    mData.get(position).setSeekerBlock(status);
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    public class MyHolder extends RecyclerView.ViewHolder {
         TextView tvRecruiterName, tvLastMessage, tvDate;
        View RemovableView, ConstantView;

          MyHolder(View itemView) {
             super(itemView);
             tvRecruiterName = mBinding.tvMessageRecruiterName;
             tvLastMessage = mBinding.tvMessageChat;
             tvDate = mBinding.tvMessageDate;
              RemovableView = mBinding.swipebleView;
              ConstantView = mBinding.constantView;
         }

       public View getRemovableView(){
            return RemovableView;
        }

        public View getConstantView(){
           return ConstantView;
        }
     }
}
