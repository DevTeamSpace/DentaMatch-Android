/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.messages;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.chat.DBModel;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ActivityChatBinding;
import com.appster.dentamatch.eventbus.ChatHistoryRetrievedEvent;
import com.appster.dentamatch.eventbus.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.eventbus.MessageAcknowledgementEvent;
import com.appster.dentamatch.eventbus.SocketConnectionEvent;
import com.appster.dentamatch.eventbus.UnblockEvent;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Appster on 09/02/17.
 * To inject activity reference.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LogUtils.makeLogTag(ChatActivity.class);
    private ActivityChatBinding mBinder;
    @Nullable
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String userId;
    private String recruiterId;
    private String recruiterName;
    @Nullable
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
        updateUI(getIntent());
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(this);
        mBinder.sendButton.setOnClickListener(this);
        mBinder.toolbarActivityChat.ivToolBarLeft.setOnClickListener(this);
        mBinder.layUnblock.setOnClickListener(this);
        mBinder.messages.setAdapter(mAdapter);
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
                    if (Utils.isConnected(ChatActivity.this)) {
                        if (SocketManager.getInstance().isConnected()) {
                            SocketManager.getInstance().sendMessage(userId, recruiterId, msg);
                            mBinder.messageInput.setText("");
                        } else {
                            ChatActivity.this.showToast(getString(R.string.error_socket_connection));
                        }
                    } else {
                        ChatActivity.this.showToast(getString(R.string.error_internet_connection));
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
        SocketManager.getInstance().detachPersonalListener();
        SocketManager.getInstance().disconnectFromChat();
        hideKeyboard();
        finish();
    }

    /**
     * Callback called in case of new message and in case of history received.
     *
     * @param event : received message from server.
     */
    @Subscribe()
    public void onNewMessageReceived(final ChatPersonalMessageReceivedEvent event) {
        if (event != null) {
            final ChatMessageModel messageModel = event.getModel();
            int messageType;
            String recruiterID;

            if (messageModel.getToID().equalsIgnoreCase(userId)) {
                messageType = Message.TYPE_MESSAGE_RECEIVED;
                recruiterID = messageModel.getFromID();
            } else {
                messageType = Message.TYPE_MESSAGE_SEND;
                recruiterID = messageModel.getToID();
            }

            /*
              Add the received message to the chat adapter for viewing.
             */
            final Message message = new Message(messageModel.getMessage(),
                    messageModel.getRecruiterName(),
                    messageModel.getMessageTime(),
                    messageModel.getMessageId(),
                    messageType);

            /*
              Insert the message received into the DB first.
             */
            DBHelper.getInstance()
                    .insertIntoDB(recruiterID, message, event.getModel().getRecruiterName(), 0, messageModel.getMessageListId());
            updateDBModel();
            scrollToBottom();
        }
    }

    private void updateDBModel() {
        dbModel = DBHelper.getInstance().getDBData(recruiterId);
        if (mBinder.messages.getAdapter() == null && dbModel != null) {
            mAdapter = new ChatAdapter(this, dbModel.getUserChats(), true);
            mBinder.messages.setAdapter(mAdapter);
        }
    }

    @Subscribe()
    public void onSentMsgAcknowledgement(final MessageAcknowledgementEvent event) {
        if (event != null && dbModel != null) {
            DBHelper.getInstance().insertIntoDB(event.getRecruiterId(), event.getMessage(), recruiterName, 0, dbModel.getMessageListId());
            updateDBModel();
            scrollToBottom();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatHistoryRetrieved(final ChatHistoryRetrievedEvent event) {
        JSONArray chatArray = event.getModel();
        try {
            if (chatArray.length() > 0) {
                for (int i = 0; i < chatArray.length(); i++) {
                    JSONObject dataObject = chatArray.getJSONObject(i);
                    ChatMessageModel chatData = Utils.parseDataForHistory(dataObject);
                    /*
                      In case of history the recruiter name comes out to be blank so we add the recruiter
                      name from the DB.
                     */
                    chatData.setRecruiterName(recruiterName);
                    onNewMessageReceived(new ChatPersonalMessageReceivedEvent(chatData));
                }
            } else {
                updateDBModel();
            }
        } catch (JSONException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        } finally {
            DBHelper.getInstance().upDateDB(recruiterId, DBHelper.IS_SYNCED, "true", null);
            updateDBModel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketConnectionStatusChanged(SocketConnectionEvent event) {
        if (event != null) {
            if (event.getStatus()) {
                showToast(getString(R.string.msg_socket_connection_success));
                updateDataFromDB();
                /*
                 Update all unread chats corresponding to this recruiterID and update server and local Db about it.
                */
                if (SocketManager.getInstance().isConnected()) {
                    SocketManager.getInstance().updateMsgRead(recruiterId, userId);
                }
            } else {
                showToast(getString(R.string.error_socket_connection));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recruiterUnblocked(UnblockEvent event) {
        if (event != null) {
            if (event.isStatus()) {
                mBinder.layUnblock.setVisibility(View.GONE);
                mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
            }
        }
    }


    private void UnBlockUser() {
        if (SocketManager.getInstance().isConnected()) {
            SocketManager.getInstance().blockUnblockUser("0", recruiterId, userId);
        } else {
            showToast(getString(R.string.error_socket_connection));
        }
    }

    private void scrollToBottom() {
        if (mAdapter != null) {
            mBinder.messages.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    private void updateUI(Intent intent) {
        if (intent.hasExtra(Constants.EXTRA_CHAT_MODEL)) {
            recruiterId = intent.getStringExtra(Constants.EXTRA_CHAT_MODEL);
            dbModel = DBHelper.getInstance().getDBData(recruiterId);
            if (recruiterId != null) {
                userId = PreferenceUtil.getUserChatId();
                /*
                  Start new adapter for the new message received.
                 */
                mBinder.messages.setAdapter(null);
                Utils.clearRecruiterNotification(this, recruiterId);

                /*
                  Connect to socket and request past chats to update the recycler view.
                 */
                SocketManager.getInstance().attachPersonalListener(this, recruiterId);

                 /*
                  Update all unread chats corresponding to this recruiterID and update server and local Db about it.
                 */
                if (SocketManager.getInstance().isConnected()) {
                    SocketManager.getInstance().updateMsgRead(recruiterId, userId);
                }

                if (dbModel != null) {
                    recruiterName = dbModel.getName();
                    mBinder.toolbarActivityChat.tvToolbarGeneralLeft.setText(recruiterName);
                    /*
                      Change the UI based on recruiter is blocked or not.
                     */
                    if (dbModel.getSeekerHasBlocked() == 1) {
                        mBinder.layUnblock.setVisibility(View.VISIBLE);
                        mBinder.layActivityChatSender.setVisibility(View.GONE);
                    } else {
                        mBinder.layUnblock.setVisibility(View.GONE);
                        mBinder.layActivityChatSender.setVisibility(View.VISIBLE);
                    }
                }
                if (getIntent().hasExtra(Constants.EXTRA_OFFICE_NAME)) {
                    String officeName = getIntent().getStringExtra(Constants.EXTRA_OFFICE_NAME);
                    if (officeName != null) {
                        mBinder.toolbarActivityChat.tvToolbarGeneralLeft.setText(officeName);
                    }
                }
                if (dbModel == null || !dbModel.isDBUpdated()) {
                    if (Utils.isConnected(this)) {
                        if (SocketManager.getInstance().isConnected()) {
                            /*
                              Show loader in case of fetching data from the server and clear all past chats of the user for corresponding
                              recruiterID.
                             */
                            ChatActivity.this.showToast(getString(R.string.msg_syncing_chat));
                            updateDataFromDB();
                        } else {
                            ChatActivity.this.showToast(getString(R.string.error_socket_connection));
                        }
                    } else {
                        ChatActivity.this.showToast(getString(R.string.error_internet_connection));
                    }
                } else {
                    updateDBModel();
                }
            }
        }
    }

    private void updateDataFromDB() {
        if (dbModel != null && dbModel.getUserChats().size() > 0) {
            Message lastMessage = dbModel.getUserChats().last();
            String fromID;
            String toID;
            if (lastMessage != null) {
                if (lastMessage.getType() == Message.TYPE_MESSAGE_RECEIVED) {
                    fromID = recruiterId;
                    toID = userId;
                } else {
                    fromID = userId;
                    toID = recruiterId;
                }
                SocketManager.getInstance().fetchPastChatsAfterMsgID(lastMessage.getMessageId(), toID, fromID);
            }
        } else {
            SocketManager.getInstance().getAllPastChats(userId, "1", recruiterId);
        }
    }
}
