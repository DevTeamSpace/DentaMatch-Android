package com.appster.dentamatch.ui.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.RealmDataBase.DBModel;
import com.appster.dentamatch.RealmDataBase.DBHelper;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ActivityChatBinding;
import com.appster.dentamatch.model.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.chat.BlockUnBlockRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;

/**
 * Created by Appster on 09/02/17.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChatActivity";
    private ActivityChatBinding mBinder;
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String userId;
    private String recruiterId;
    private DBModel dbModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initViews();
        mLayoutManager.setStackFromEnd(true);
        mBinder.messages.setLayoutManager(mLayoutManager);
        mBinder.messages.setAdapter(mAdapter);

        if (getIntent().hasExtra(Constants.EXTRA_CHAT_MODEL)) {
            String recruiterID = getIntent().getStringExtra(Constants.EXTRA_CHAT_MODEL);
            dbModel = DBHelper.getInstance().getDBData(recruiterID);
        }

        /**
         * Change the UI based on recruiter is blocked or not.
         */
        if (dbModel.getSeekerHasBlocked() == 1) {
            mBinder.layUnblock.setVisibility(View.VISIBLE);
            mBinder.layActivityChatSender.setVisibility(View.GONE);
        } else {
            mBinder.layUnblock.setVisibility(View.GONE);
            mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
        }

        userId = PreferenceUtil.getUserChatId();
        recruiterId = dbModel.getRecruiterId();
        mBinder.toolbarActivityChat.tvToolbarGeneralLeft.setText(dbModel.getName());

    }



    private void initViews() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatAdapter(this);

        mBinder.sendButton.setOnClickListener(this);
        mBinder.toolbarActivityChat.ivToolBarLeft.setOnClickListener(this);
        mBinder.layUnblock.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public String getActivityName() {
        return null;
    }

    /**
     * Registering EventBus to receive chat updates.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        /**
         * Connect to socket and request past chats to update the recycler view.
         */
        SocketManager.getInstance().attachPersonalListener(this, userId);
        SocketManager.getInstance().getAllPastChats(userId, "1", recruiterId);
    }

    /**
     * UnRegistering EventBus to stop receive chat updates.
     */
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);

        /**
         * Disconnect to socket and request past chats to update the recycler view.
         */
        SocketManager.getInstance().detachPersonalListener();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lay_unblock:
                UnBlockUser();
                break;

            case R.id.send_button:
                String msg = mBinder.messageInput.getText().toString().trim();

                if (!TextUtils.isEmpty(msg)) {
                    SocketManager.getInstance().sendMessage(userId, recruiterId, msg);
                    mBinder.messageInput.setText("");
                    addMessageToAdapter(msg, Message.TYPE_MESSAGE_SEND, String.valueOf(System.currentTimeMillis()));
                }

                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            default:
                break;
        }
    }


    @Subscribe
    public void onNewMessageReceived(ChatPersonalMessageReceivedEvent event) {
        if (event != null) {
            ChatMessageModel messageModel = event.getModel();

            if (messageModel.getToID().equalsIgnoreCase(userId)) {
                addMessageToAdapter(messageModel.getMessage(), Message.TYPE_MESSAGE_RECEIVED, messageModel.getMessageTime());
            } else {
                addMessageToAdapter(messageModel.getMessage(), Message.TYPE_MESSAGE_SEND, messageModel.getMessageTime());
            }
        }

    }

    private void UnBlockUser() {
        BlockUnBlockRequest request = new BlockUnBlockRequest();
        request.setBlockStatus(String.valueOf(0));
        request.setRecruiterId(recruiterId);

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices client = RequestController.createService(AuthWebServices.class);
        client.blockUnBlockUser(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {

                if (response.getStatus() == 1) {
                    DBHelper.getInstance().upDateDB(recruiterId, DBHelper.IS_RECRUITED_BLOCKED, "0", null);
                    mBinder.layUnblock.setVisibility(View.GONE);
                    mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
                } else {
                    showToast(response.getMessage());
                }

            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void scrollToBottom() {
        mBinder.messages.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void addMessageToAdapter(String userText, int messageType, String time) {
        Message message = new Message.Builder(messageType)
                .message(userText)
                .time(time)
                .build();
        mAdapter.addMessage(message);
        scrollToBottom();
    }
}
