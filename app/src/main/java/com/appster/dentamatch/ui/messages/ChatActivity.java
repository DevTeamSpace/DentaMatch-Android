package com.appster.dentamatch.ui.messages;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.RealmDataBase.DBHelper;
import com.appster.dentamatch.RealmDataBase.DBModel;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ActivityChatBinding;
import com.appster.dentamatch.model.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.model.MessageAcknowledgementEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.chat.BlockUnBlockRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

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
    private String recruiterName;
    private DBModel dbModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initViews();
        mLayoutManager.setStackFromEnd(true);
        mBinder.messages.setLayoutManager(mLayoutManager);
        mBinder.messages.setAdapter(mAdapter);
        updateUI(getIntent());
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatAdapter(this);

        mBinder.sendButton.setOnClickListener(this);
        mBinder.toolbarActivityChat.ivToolBarLeft.setOnClickListener(this);
        mBinder.layUnblock.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       updateUI(intent);

    }

    @Override
    protected void onPause() {
        SocketManager.getInstance().setAttachedActivityStatus(SocketManager.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocketManager.getInstance().setAttachedActivityStatus(SocketManager.ON_RESUME);

    }

    @Override
    public String getActivityName() {
        return null;
    }


    /**
     * UnRegistering EventBus to stop receive chat updates.
     */
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

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
                    if(SocketManager.getInstance().isConnected()){
                        SocketManager.getInstance().sendMessage(userId, recruiterId, msg);
                        mBinder.messageInput.setText("");
                    }else{
                        ChatActivity.this.showToast("Internet connection problem, please check your connection.");
                    }

                }

                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /**
         * Disconnect to socket and request past chats to update the recycler view.
         */
        SocketManager.getInstance().detachPersonalListener();
        hideKeyboard();
        finish();
//        startActivity(new Intent(this, HomeActivity.class).putExtra(Constants.EXTRA_FROM_CHAT, true));
    }

    /**
     * Callback called in case of new message and in case of history received.
     *
     * @param event : received message from server.
     */
    @Subscribe
    public void onNewMessageReceived(ChatPersonalMessageReceivedEvent event) {
        if (event != null) {
            ChatMessageModel messageModel = event.getModel();
            int messageType = 0;

            if (messageModel.getToID().equalsIgnoreCase(userId)) {
                messageType = Message.TYPE_MESSAGE_RECEIVED;
            } else {
                messageType = Message.TYPE_MESSAGE_SEND;
            }

            /**
             * Add the received message to the chat adapter for viewing.
             */
            Message message = new Message(messageModel.getMessage(),
                    messageModel.getRecruiterName(),
                    messageModel.getMessageTime(),
                    messageModel.getMessageId(),
                    messageType);


            /**
             * Insert the message received into the DB first.
             */
            DBHelper.getInstance().insertIntoDB(recruiterId, message, event.getModel().getRecruiterName(), 0);
            addMessageToAdapter(message);
        }

    }

    @Subscribe
    public void onSentMsgAcknowledgement(final MessageAcknowledgementEvent event) {
        if (event != null) {
            DBHelper.getInstance().insertIntoDB(recruiterId, event.getmMessage(), recruiterName, 0);
            addMessageToAdapter(event.getmMessage());

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

    private void addMessageToAdapter(Message message) {
        mAdapter.addMessage(message);
        scrollToBottom();
    }

    private void updateUI(Intent intent) {
        if (intent.hasExtra(Constants.EXTRA_CHAT_MODEL)) {
            recruiterId = getIntent().getStringExtra(Constants.EXTRA_CHAT_MODEL);
            dbModel = DBHelper.getInstance().getDBData(recruiterId);
            userId = PreferenceUtil.getUserChatId();
            recruiterName = dbModel.getName();
            mBinder.toolbarActivityChat.tvToolbarGeneralLeft.setText(recruiterName);

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


            /**
             * Connect to socket and request past chats to update the recycler view.
             */
            SocketManager.getInstance().attachPersonalListener(this, recruiterId);

            if (!dbModel.isDBUpdated()) {

                if (Utils.isConnected(this)) {
                    SocketManager.getInstance().getAllPastChats(userId, "1", recruiterId);
                    DBHelper.getInstance().upDateDB(recruiterId, DBHelper.IS_SYNCED, "true", null);
                } else {
                    for (Message message : dbModel.getUserChats()) {
                        addMessageToAdapter(message);
                    }
                }

            } else {
                for (Message message : dbModel.getUserChats()) {
                    addMessageToAdapter(message);
                }
            }

        }
    }
}
