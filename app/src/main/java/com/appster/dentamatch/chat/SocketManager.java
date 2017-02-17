package com.appster.dentamatch.chat;


import android.app.Activity;
import android.content.Intent;

import com.appster.dentamatch.RealmDataBase.DBHelper;
import com.appster.dentamatch.model.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.model.MessageAcknowledgementEvent;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.messages.ChatMessageModel;
import com.appster.dentamatch.ui.messages.Message;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by ramkumar on 06/02/17.
 */

public class SocketManager {
    private static final String TAG = "DentaChat";
    private final String SOCKET_ERROR = "socket is null";
    private final String SOCKET_CONNECT = "Socket connected";
    private final String SOCKET_DISCONNECT = "Socket disconnected";
    private final String SOCKET_CONNECTION_ERROR = "Socket connection error";
    private final String SOCKET_ACKNOWLEDGEMENT = "Socket_acknowledgement_received";

    private final String PARAM_FROM_ID = "fromId";
    private final String PARAM_TO_ID = "toId";
    private final String PARAM_USERID = "userId";
    private final String PARAM_USERNAME = "userName";
    private final String PARAM_SENT_TIME = "sentTime";
    private final String PARAM_USER_MSG = "message";
    private final String PARAM_PAGE = "pageNo";
    private final String PARAM_RECRUITER_NAME = "fromName";
    private final String PARAM_MESSAGE_ID = "messageId";

    private final String EMIT_USER_HISTORY = "getHistory";
    private final String EMIT_INIT = "init";
    private final String EMIT_SEND_MSG = "sendMessage";
    private final String EMIT_UPDATE_READ_COUNT = "updateReadCount";

    private final String EVENT_NEW_MESSAGE = "receiveMessage";
    private final String EVENT_CHAT_HISTORY = "getMessages";

    //    private static String CHAT_SERVER_URL = "http://172.16.16.188:3000";
    private static String CHAT_SERVER_URL = "http://dev.dentamatch.co:3000";

    private static Socket mSocket;
    private static SocketManager socketManager;
    private Emitter.Listener onHistory;
    private boolean isConnected;
    private Activity attachedActivity;
    private String attachedRecruiterID;
    private Activity attachedGlobalActivity;

    private SocketManager() {
        getSocket();
    }

    public static SocketManager getInstance() {
        if (socketManager == null) {
            synchronized (SocketManager.class) {

                if (socketManager == null) {
                    socketManager = new SocketManager();
                }
            }
        }

        return socketManager;
    }

    public boolean isConnected() {
        return isConnected;
    }

    private static Socket getSocket() {
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return mSocket;
    }

    public boolean isSocketConnected(){
        return isConnected;
    }

    public void connect(Activity act) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        registerEvents();
        mSocket.connect();
        attachedGlobalActivity = act;

    }

    public void disconnect() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        unRegisterEvents();
        mSocket.disconnect();
    }

    /**
     * Reset all DB status to sync status needed, in order to retreive data from the server on
     * Socket Connected.
     */
    private void raiseSyncNeeded(){
        attachedGlobalActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance().setSyncNeeded();
            }
        });
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_CONNECT);
            isConnected = true;
            init();
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_DISCONNECT);
            isConnected = false;
            raiseSyncNeeded();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_CONNECTION_ERROR);
            isConnected = false;
            raiseSyncNeeded();
        }
    };

    private Ack messageSentAcknowledgement = new Ack() {
        @Override
        public void call(final Object... args) {
            attachedActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /**
                     * Insert the message sent into the DB .
                     */
                    final JSONObject jsonObject = (JSONObject) args[0];
                    LogUtils.LOGD(TAG, SOCKET_ACKNOWLEDGEMENT + args[0]);
                    ChatMessageModel model = parseData(jsonObject);
                    Message message = new Message(model.getMessage(),
                            model.getRecruiterName(),
                            model.getMessageTime(),
                            model.getMessageId(),
                            Message.TYPE_MESSAGE_SEND);

                    EventBus.getDefault().post(new MessageAcknowledgementEvent(message));
                }
            });

        }
    };

    /**
     * this listener is called  when a new message is transmitted from he server.
     */
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, EVENT_NEW_MESSAGE + ":" + args[0]);
            final JSONObject jsonObject = (JSONObject) args[0];

            final ChatMessageModel model = parseData(jsonObject);

            /**
             * If the user ID matches the attached activities userID then send message to the chatActivity to update adapter.
             * Else send the message to the global message list listener to update data.
             */
            if (attachedRecruiterID != null && model.getFromID().equalsIgnoreCase(attachedRecruiterID)) {
                if (attachedActivity != null) {
                    attachedActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * update the Unread status to read when the user has seen the message. In this case we
                             * add the message after in the post event of eventBus because we need to update the chat adapter
                             * in the UI, for the user to read the message.
                             */
                            updateMsgRead(model.getFromID(), model.getToID());
                            EventBus.getDefault().post(new ChatPersonalMessageReceivedEvent(model));
                        }
                    });
                }
            } else {

                /**
                 * In case a global message has been received we store it to the DB directly as no UI needs to be updated,
                 * The DB changes are directly reflected in the Adapter.
                 */
                if (attachedGlobalActivity != null) {
                    attachedGlobalActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message(model.getMessage(),
                                    model.getRecruiterName(),
                                    model.getMessageTime(),
                                    model.getMessageId(),
                                    Message.TYPE_MESSAGE_RECEIVED);

                            DBHelper.getInstance().insertIntoDB(model.getFromID(), message, model.getRecruiterName(), 1);

                            Intent intent = new Intent(attachedGlobalActivity, HomeActivity.class);
                            intent.putExtra(Constants.EXTRA_FROM_CHAT, model.getFromID());
                            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Utils.showNotification(attachedGlobalActivity, model.getRecruiterName(), model.getMessage(), intent);

                        }
                    });
                }
            }

        }
    };

    public void attachPersonalListener(Activity act, String userID) {
        attachedActivity = act;
        setHistoryListener();
        this.attachedRecruiterID = userID;

        /**
         * Attach listener for history.
         */
        if (!mSocket.hasListeners(EVENT_CHAT_HISTORY)) {
            mSocket.on(EVENT_CHAT_HISTORY, onHistory);
        }
    }

    public void detachPersonalListener() {
        attachedActivity = null;
        this.attachedRecruiterID = null;
        /**
         * Remove history listener.
         */
        if (mSocket.hasListeners(EVENT_CHAT_HISTORY)) {
            mSocket.off(EVENT_CHAT_HISTORY, onHistory);
        }
    }


    private void registerEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(EVENT_NEW_MESSAGE, onNewMessage);
    }

    private void unRegisterEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(EVENT_NEW_MESSAGE, onNewMessage);

    }

    public void sendMessage(String fromId, String toId, String msg) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_FROM_ID, fromId);
        hashMap.put(PARAM_TO_ID, toId);
        hashMap.put(PARAM_USER_MSG, msg);
        mSocket.emit(EMIT_SEND_MSG, new JSONObject(hashMap), messageSentAcknowledgement);

    }

    public void init() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_USERID, PreferenceUtil.getUserChatId());
        hashMap.put(PARAM_USERNAME, PreferenceUtil.getFirstName());
        JSONObject object = new JSONObject(hashMap);
        mSocket.emit(EMIT_INIT, object);
        LogUtils.LOGD(TAG, EMIT_INIT + ":" + object.toString());

    }

    public void getAllPastChats(String userID, String pageNo, String toID) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PAGE, pageNo);
        hashMap.put(PARAM_FROM_ID, userID);
        hashMap.put(PARAM_TO_ID, toID);
        JSONObject object = new JSONObject(hashMap);
        mSocket.emit(EMIT_USER_HISTORY, object);
        LogUtils.LOGD(TAG, EMIT_USER_HISTORY + ":" + object.toString());
    }

    private ChatMessageModel parseData(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString(PARAM_FROM_ID));
            model.setRecruiterName(messageData.getString(PARAM_RECRUITER_NAME));
            model.setToID(messageData.getString(PARAM_TO_ID));
            model.setMessageTime(messageData.getString(PARAM_SENT_TIME));
            model.setMessage(messageData.getString(PARAM_USER_MSG));
            model.setMessageId(messageData.getString(PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    private ChatMessageModel parseDataForHistory(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString(PARAM_FROM_ID));
            model.setToID(messageData.getString(PARAM_TO_ID));
            model.setMessageTime(messageData.getString(PARAM_SENT_TIME));
            model.setMessage(messageData.getString(PARAM_USER_MSG));
            model.setMessageId(messageData.getString(PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }


    private void setHistoryListener() {
        onHistory = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                LogUtils.LOGD(TAG, EVENT_CHAT_HISTORY + ":" + args[0]);
                final JSONArray jsonArray = (JSONArray) args[0];

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        final JSONObject dataObject = jsonArray.getJSONObject(i);

                        if (attachedActivity != null) {

                            attachedActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new ChatPersonalMessageReceivedEvent(parseDataForHistory(dataObject)));
                                }
                            });


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        };
    }

    /**
     * Messages sent from recruiter ID to user ID, need to be updated.
     *
     * @param fromId
     * @param toId
     */
    public void updateMsgRead(String fromId, String toId) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_FROM_ID, fromId);
        hashMap.put(PARAM_TO_ID, toId);
        mSocket.emit(EMIT_UPDATE_READ_COUNT, new JSONObject(hashMap));

    }
}
