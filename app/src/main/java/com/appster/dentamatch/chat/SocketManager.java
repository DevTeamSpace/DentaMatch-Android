package com.appster.dentamatch.chat;


import android.app.Activity;

import com.appster.dentamatch.model.ChatMessageReceivedEvent;
import com.appster.dentamatch.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

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

    private final String PARAM_FROM_ID = "fromId";
    private final String PARAM_TO_ID = "toId";
    private final String PARAM_USERID = "userId";
    private final String PARAM_USERNAME = "userName";
    private final String PARAM_SENT_TIME = "sentTime";
    private final String PARAM_USER_MSG = "message";
    private final String PARAM_PAGE = "pageNo";

    private final String EMIT_USER_HISTORY = "getHistory";
    private final String EMIT_INIT = "init";
    private final String EMIT_SEND_MSG = "sendMessage";

    private final String EVENT_NEW_MESSAGE = "receiveMessage";
    private final String EVENT_CHAT_HISTORY = "getMessages";

    //    private static String CHAT_SERVER_URL = "http://172.16.16.188:3000";
    private static String CHAT_SERVER_URL = "http://dev.dentamatch.co:3000";

    private static Socket mSocket;
    private static SocketManager socketManager;
    private Emitter.Listener onNewMessage;
    private Emitter.Listener onHistory;
    private boolean isConnected;


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

    public void connect() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        registerEvents();
        mSocket.connect();
    }

    public void disconnect() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }
        unRegisterEvents();
        mSocket.disconnect();
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
        mSocket.off(EVENT_CHAT_HISTORY, onHistory);
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
        mSocket.emit(EMIT_SEND_MSG, new JSONObject(hashMap));
    }

    public void init(String UserID, String UserName) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, SOCKET_ERROR);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_USERID, UserID);
        hashMap.put(PARAM_USERNAME, UserName);
        JSONObject object = new JSONObject(hashMap);
        mSocket.emit(EMIT_INIT, object);
        LogUtils.LOGD(TAG, EMIT_INIT +":"+ object.toString());

    }

    public void getAllPastChats(String userID, String pageNo, String toID){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PAGE, pageNo);
        hashMap.put(PARAM_FROM_ID, userID);
        hashMap.put(PARAM_TO_ID, toID);
        JSONObject object = new JSONObject(hashMap);
        mSocket.emit(EMIT_USER_HISTORY, object);
        LogUtils.LOGD(TAG, EMIT_USER_HISTORY+ ":" + object.toString());
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_CONNECT);
            isConnected = true;
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_DISCONNECT);
            isConnected = false;
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, SOCKET_CONNECTION_ERROR);
        }
    };



    public void attachActivityToSocket(final Activity act){
        setChatMessageListener(act);
        setHistoryListener(act);
    }

    private void  setHistoryListener(final Activity act){
        onHistory = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                LogUtils.LOGD(TAG,EVENT_CHAT_HISTORY+":"+args[0]);
                final JSONArray jsonArray = (JSONArray) args[0];

                   act.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           for(int i = 0; i < jsonArray.length(); i++){
                               try {
                                   JSONObject messageData = jsonArray.getJSONObject(i);
                                  String fromId = messageData.getString(PARAM_FROM_ID);
                                   String toId = messageData.getString(PARAM_TO_ID);
                                   String sentTime = messageData.getString(PARAM_SENT_TIME);
                                   String message = messageData.getString(PARAM_USER_MSG);
                                   EventBus.getDefault().post(new ChatMessageReceivedEvent(fromId, toId, message, sentTime));

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   });


            }
        };

        if(!mSocket.hasListeners(EVENT_CHAT_HISTORY)) {
            mSocket.on(EVENT_CHAT_HISTORY, onHistory);
        }

    }

    private  void setChatMessageListener(final Activity act){
         onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        LogUtils.LOGD(TAG, EVENT_NEW_MESSAGE+":" + data);

                        String fromId ;
                        String toId ;
                        String sentTime ;
                        String message;
                        try {
                            fromId = data.getString(PARAM_FROM_ID);
                            toId = data.getString(PARAM_TO_ID);
                            sentTime = data.getString(PARAM_SENT_TIME);
                            message = data.getString(PARAM_USER_MSG);
                            EventBus.getDefault().post(new ChatMessageReceivedEvent(fromId, toId, message, sentTime));
                        } catch (JSONException e) {
                            LogUtils.LOGD(TAG, "JSON parse error " + e.getMessage());
                        }
                    }
                });


            }
        };

        if (!mSocket.hasListeners(EVENT_NEW_MESSAGE)) {
            mSocket.on(EVENT_NEW_MESSAGE, onNewMessage);
        }
    }



}
