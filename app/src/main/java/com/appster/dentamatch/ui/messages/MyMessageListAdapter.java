package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.RealmDataBase.DBHelper;
import com.appster.dentamatch.RealmDataBase.DBModel;
import com.appster.dentamatch.databinding.ItemMessageListBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.chat.BlockUnBlockRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import retrofit2.Call;

/**
 * Created by Appster on 15/02/17.
 */

public class MyMessageListAdapter extends RealmRecyclerViewAdapter<DBModel, MyMessageListAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private OrderedRealmCollection<DBModel> messagesData;
    private ItemMessageListBinding mBinding;
    private Context mContext;

    public MyMessageListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<DBModel> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        messagesData = data;
        mContext = context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_list, parent, false);
        return new MyMessageListAdapter.MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        if (messagesData.get(position) != null) {
            DBModel dataModel = messagesData.get(position);
            holder.tvRecruiterName.setText(dataModel.getName());
            holder.tvLastMessage.setText(dataModel.getLastMessage());
            holder.tvDate.setText(Utils.convertUTCToTimeLabel(dataModel
                            .getUserChats()
                            .get(dataModel.getUserChats().size() - 1).getmMessageTime()));

            dataModel.getUnReadChatCount();

            if(dataModel.getUnReadChatCount() == 0){
                holder.tvUnreadChatCount.setVisibility(View.GONE);
            }else if(dataModel.getUnReadChatCount() >= 100){
                holder.tvUnreadChatCount.setVisibility(View.VISIBLE);
                holder.tvUnreadChatCount.setText("999");
            }else{
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
        if (messagesData.size() > 0) {
            return messagesData.size();
        }

        return 0;
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
        if(messagesData.get(position).getSeekerHasBlocked() == 0) {
            Alert.createYesNoAlert(mContext,
                    mContext.getString(R.string.block),
                    mContext.getString(R.string.cancel),
                    null,
                    mContext.getString(R.string.msg_block_alert),
                    new Alert.OnAlertClickListener() {

                        @Override
                        public void onPositive(DialogInterface dialog) {
                            blockUnBlockUser("1", messagesData.get(position).getRecruiterId());
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
        }
        return false;
    }

    private void blockUnBlockUser(final String status, final String recruiterID){
        BlockUnBlockRequest request = new BlockUnBlockRequest();
        request.setBlockStatus(String.valueOf(status));
        request.setRecruiterId(String.valueOf(recruiterID));

        ((BaseActivity)mContext). processToShowDialog("", mContext.getString(R.string.please_wait), null);
        AuthWebServices client = RequestController.createService(AuthWebServices.class);
        client.blockUnBlockUser(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity) mContext).showToast(response.getMessage());

                if (response.getStatus() == 1) {
                    DBHelper.getInstance().upDateDB(recruiterID, DBHelper.IS_RECRUITED_BLOCKED, status, null);
                }

            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvRecruiterName, tvLastMessage, tvDate, tvUnreadChatCount;
        View RemovableView, ConstantView;

        MyHolder(View itemView) {
            super(itemView);

            tvRecruiterName = mBinding.tvMessageRecruiterName;
            tvUnreadChatCount = mBinding.tvMessageChatCount;
            tvLastMessage = mBinding.tvMessageChat;
            tvDate = mBinding.tvMessageDate;
            RemovableView = mBinding.swipebleView;
            ConstantView = mBinding.constantView;
        }
    }
}
