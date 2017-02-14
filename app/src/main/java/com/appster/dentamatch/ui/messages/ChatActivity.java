package com.appster.dentamatch.ui.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ActivityChatBinding;
import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.model.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.model.ChatUserUnBlockedEvent;
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
    private ChatListModel mChatModel;
    String userId;
    String recruiterId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initViews();
        mLayoutManager.setStackFromEnd(true);
        mBinder.messages.setLayoutManager(mLayoutManager);
        mBinder.messages.setAdapter(mAdapter);

        if (getIntent().hasExtra(Constants.EXTRA_CHAT_MODEL)) {
            mChatModel = getIntent().getParcelableExtra(Constants.EXTRA_CHAT_MODEL);
        }

        /**
         * Change the Ui based on recruiter is blocked or not.
         */
        if (mChatModel.getRecruiterBlock() == 1) {
            mBinder.layUnblock.setVisibility(View.VISIBLE);
            mBinder.layActivityChatSender.setVisibility(View.GONE);
        } else {
            mBinder.layUnblock.setVisibility(View.GONE);
            mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
        }

        userId = PreferenceUtil.getUserChatId();
        recruiterId = String.valueOf(mChatModel.getRecruiterId());
        mBinder.toolbarActivityChat.tvToolbarGeneralLeft.setText(mChatModel.getName());

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
                blockUnBlockUser(0, mChatModel.getRecruiterId());
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
                addMessageToAdapter(messageModel.getMessage(), Message.TYPE_MESSAGE_SEND, messageModel.getMessageTime());
            } else {
                addMessageToAdapter(messageModel.getMessage(), Message.TYPE_MESSAGE_RECEIVED, messageModel.getMessageTime());
            }
        }

    }

    private void blockUnBlockUser(final int status, final int recruiterID) {
        BlockUnBlockRequest request = new BlockUnBlockRequest();
        request.setBlockStatus(String.valueOf(status));
        request.setRecruiterId(String.valueOf(recruiterID));

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices client = RequestController.createService(AuthWebServices.class);
        client.blockUnBlockUser(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {

                if (response.getStatus() == 1) {
                    mChatModel.setRecruiterBlock(0);
                    mBinder.layUnblock.setVisibility(View.GONE);
                    mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new ChatUserUnBlockedEvent(recruiterID));
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
