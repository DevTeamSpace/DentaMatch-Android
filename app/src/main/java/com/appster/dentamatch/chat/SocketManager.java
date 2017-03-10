package com.appster.dentamatch.chat;


import android.app.Activity;
import android.content.Intent;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.model.ChatHistoryRetrievedEvent;
import com.appster.dentamatch.model.ChatPersonalMessageReceivedEvent;
import com.appster.dentamatch.model.MessageAcknowledgementEvent;
import com.appster.dentamatch.model.SocketConnectionEvent;
import com.appster.dentamatch.model.UnblockEvent;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.messages.ChatActivity;
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
    private final String SOCKET_MESSAGE_ACKNOWLEDGEMENT = "Socket_acknowledgement_received";
    private final String SOCKET_PAST_CHAT_ACKNOWLEDGEMENT = "Socket_past_chat_acknowledgement_received";

    public static final String PARAM_FROM_ID = "fromId";
    public static final String PARAM_TO_ID = "toId";
    public static final String PARAM_USERID = "userId";
    public static final String PARAM_USERNAME = "userName";
    public static final String PARAM_SENT_TIME = "sentTime";
    public static final String PARAM_USER_MSG = "message";
    public static final String PARAM_PAGE = "pageNo";
    public static final String PARAM_RECRUITER_NAME = "fromName";
    public static final String PARAM_MESSAGE_ID = "messageId";

    public static final int ON_RESUME = 0;
    public static final int ON_PAUSE = 1;

    private final String EMIT_USER_HISTORY = "getHistory";
    private final String EMIT_INIT = "init";
    private final String EMIT_SEND_MSG = "sendMessage";
    private final String EMIT_UPDATE_READ_COUNT = "updateReadCount";
    private final String EMIT_GET_LEFT_MESSAGES = "getLeftMessages";
    private final String EMIT_BLOCK_UNBLOCK_USER = "blockUnblock";

    private final String EVENT_NEW_MESSAGE = "receiveMessage";
    private final String EVENT_CHAT_HISTORY = "getMessages";

    private static String CHAT_SERVER_URL = BuildConfig.CHAT_URL;


    private static Socket mSocket;
    private static SocketManager socketManager;
    private Emitter.Listener onHistory;
    private boolean isConnected = false;
    private int attachedActivityStatus;
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


    public void connect(Activity act) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        if(!isConnected) {
            registerConnectionEvents();
            mSocket.connect();
            attachedGlobalActivity = act;
        }

    }

    public void disconnect() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        isConnected = false;
        unRegisterConnectionEvents();
        mSocket.disconnect();
    }

    /**
     * Reset all DB status to sync status needed, in order to retreive data from the server on
     * Socket Connected.
     */
    private void raiseSyncNeeded() {
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
            unRegisterListenerEvents();
            registerListenerEvents();

            if(!isConnected) {
                isConnected = true;
                raiseSyncNeeded();
            }
            /**
             * Notify user if user is on chat screen about the socket connection .
             */
            if(attachedActivity != null){
                EventBus.getDefault().post(new SocketConnectionEvent(isConnected));
            }
            init();
        }
    };

    public void blockUnblockUser(final String blockStatus, final String toId, final String fromId) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("blockStatus", blockStatus);
        hashMap.put(PARAM_TO_ID, toId);
        hashMap.put(PARAM_FROM_ID, fromId);

        mSocket.emit(EMIT_BLOCK_UNBLOCK_USER, new JSONObject(hashMap), new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.LOGD(TAG, "" + args[0]);
                JSONObject object = (JSONObject) args[0];

                if (attachedGlobalActivity != null) {
                    attachedGlobalActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper.getInstance().upDateDB(toId, DBHelper.IS_RECRUITED_BLOCKED, blockStatus, null);

                            if(blockStatus.equalsIgnoreCase("0")){
                                EventBus.getDefault().post(new UnblockEvent(true));
                                ((BaseActivity)attachedGlobalActivity).showToast("Recruiter Unblocked");
                            }else{
                                ((BaseActivity)attachedGlobalActivity).showToast("Recruiter Blocked");
                            }

                        }
                    });
                }
            }
        });

    }



    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_DISCONNECT);
            isConnected = false;
            /**
             * Notify user if user is on chat screen about the socket connection event.
             */
            if(attachedActivity != null){
                EventBus.getDefault().post(new SocketConnectionEvent(isConnected));
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_CONNECTION_ERROR);
            isConnected = false;
            disconnectFromChat();

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
                    LogUtils.LOGD(TAG, SOCKET_MESSAGE_ACKNOWLEDGEMENT + args[0]);

                    if(jsonObject.has("blocked")){
                        ((BaseActivity)attachedActivity).showToast("Recruiter has blocked you from messaging");
                    }else {
                        ChatMessageModel model = Utils.parseData(jsonObject);

                        Message message = new Message(model.getMessage(),
                                model.getRecruiterName(),
                                model.getMessageTime(),
                                model.getMessageId(),
                                Message.TYPE_MESSAGE_SEND);

                        EventBus.getDefault().post(new MessageAcknowledgementEvent(message, model.getToID()));
                    }
                }
            });

        }
    };

    private Ack pastChatReceivedAcknowledgement = new Ack() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_PAST_CHAT_ACKNOWLEDGEMENT + args[0]);
            final JSONArray jsonArray = (JSONArray) args[0];
            /**
             * chat json is not empty
             */
                if (attachedActivity != null) {

                    attachedActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new ChatHistoryRetrievedEvent(jsonArray));
                        }
                    });
                }

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
            final ChatMessageModel model;

            /**
             * A new message from a new recruiter will have a messageListId in the jsonObject and other messages wont.
             * So we parse data based on the response type data we have received.
             */
            if(jsonObject.has("messageListId")){
               model = Utils.parseDataForNewRecruiterMessage(jsonObject);
            }else{
                model = Utils.parseData(jsonObject);
            }

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

                            /**
                             * in case the activity goes into background and socket is still connected , update user about message through notification.
                             */
                            if (attachedActivityStatus == ON_PAUSE) {
                                Intent intent = new Intent(attachedActivity, ChatActivity.class);
                                intent.putExtra(Constants.EXTRA_CHAT_MODEL, model.getFromID());
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                Utils.showNotification(attachedActivity, model.getRecruiterName(), model.getMessage(), intent, model.getFromID());
                            }
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


                            /**
                             * Don't shoot another notification in case the message has the same ID. Server has the tendency
                             * to send the same msg multiple times.
                             */
                            if (!DBHelper.getInstance().checkIfMessageAlreadyExists(model.getFromID(), message)) {
                                DBModel dbModel = DBHelper.getInstance().getDBData(model.getFromID());

                                if(dbModel != null) {
                                    if (dbModel.isDBUpdated()) {
                                        DBHelper.getInstance().insertIntoDB(model.getFromID(),
                                                message,
                                                model.getRecruiterName(),
                                                1,
                                                model.getMessageListId());

                                    } else {
                                        /**
                                         * In case there is a sync needed , we update only the listing of messages and not the chat array
                                         * as it will be updated with the history fetched.
                                         */
                                        DBHelper.getInstance().updateRecruiterDetails(model.getFromID(),
                                                model.getRecruiterName(),
                                                1,
                                                model.getMessageListId(),
                                                model.getMessage(),
                                                model.getMessageTime(),
                                                false);
                                    }
                                }else{
                                    /**
                                     * In case the message is from a new recruiter, directly insert it into the DB as it does not
                                     * require any syncing.
                                     */
                                    DBHelper.getInstance().insertIntoDB(model.getFromID(),
                                            message,
                                            model.getRecruiterName(),
                                            1,
                                            model.getMessageListId());
                                }

                                Intent intent = new Intent(attachedGlobalActivity, HomeActivity.class);
                                intent.putExtra(Constants.EXTRA_FROM_CHAT, model.getFromID());
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                Utils.showNotification(attachedGlobalActivity, model.getRecruiterName(), model.getMessage(), intent, model.getFromID());
                            }
                        }
                    });
                }
            }

        }
    };

    private Emitter.Listener onUserSessionExpired = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            try {
            LogUtils.LOGD(TAG,""+args[0]);
            JSONObject object = (JSONObject) args[0];
                boolean status = Boolean.parseBoolean(object.getString("logout"));
                if(status){
                    if(attachedActivity != null){
                        attachedActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((BaseActivity)attachedActivity).localLogOut();
                            }
                        });
                    }else if(attachedGlobalActivity != null){
                        attachedGlobalActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((BaseActivity)attachedGlobalActivity).localLogOut();
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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


    private void registerListenerEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        mSocket.on("logoutPreviousSession", onUserSessionExpired);
        mSocket.on(EVENT_NEW_MESSAGE, onNewMessage);
    }

    private void registerConnectionEvents(){
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    private void unRegisterListenerEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        mSocket.off(EVENT_NEW_MESSAGE, onNewMessage);
        mSocket.off("logoutPreviousSession", onUserSessionExpired);

    }

    private void unRegisterConnectionEvents(){
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
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

    public void disconnectFromChat() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fromId", PreferenceUtil.getUserChatId());
        mSocket.emit("notOnChat", new JSONObject(hashMap), new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.LOGD(TAG, "notOnChat" + ":" + args[0]);
            }
        });
    }

    private void init() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_USERID, PreferenceUtil.getUserChatId());
        hashMap.put(PARAM_USERNAME, PreferenceUtil.getFirstName());
        hashMap.put("userType", "1");
        JSONObject object = new JSONObject(hashMap);
        mSocket.emit(EMIT_INIT, object, new Ack() {
            @Override
            public void call(Object... args) {
                //TODO acknowledgment for init success
            }
        });
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

    public void fetchPastChatsAfterMsgID(String messageID, String toID, String fromID) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_MESSAGE_ID, messageID);
        hashMap.put(PARAM_FROM_ID, fromID);
        hashMap.put(PARAM_TO_ID, toID);
        mSocket.emit(EMIT_GET_LEFT_MESSAGES, new JSONObject(hashMap), pastChatReceivedAcknowledgement);
    }


    private void setHistoryListener() {
        onHistory = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.LOGD(TAG, EVENT_CHAT_HISTORY + ":" + args[0]);
                final JSONArray jsonArray = (JSONArray) args[0];

                if (attachedActivity != null) {
                    attachedActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new ChatHistoryRetrievedEvent(jsonArray));
                        }
                    });
                }
            }
        };
    }

    /**
     * Messages sent from recruiter ID to user ID, need to be updated.
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
        mSocket.emit(EMIT_UPDATE_READ_COUNT, new JSONObject(hashMap), new Ack() {
            @Override
            public void call(Object... args) {

                if (attachedGlobalActivity != null) {
                    attachedGlobalActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * Update the unread count once the user has opened the chat activity.
                             */
                            DBHelper.getInstance().upDateDB(attachedRecruiterID, DBHelper.UNREAD_MSG_COUNT, "0", null);
                        }
                    });
                } else {
                    LogUtils.LOGD(TAG, "attachedGlobalActivity == null");
                }
            }
        });
    }

    public void setAttachedActivityStatus(int status) {
        attachedActivityStatus = status;
    }
}
