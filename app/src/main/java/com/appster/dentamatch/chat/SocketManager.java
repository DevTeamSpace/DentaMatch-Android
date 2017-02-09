package com.appster.dentamatch.chat;


import android.content.Context;
import android.widget.Toast;

import com.appster.dentamatch.ui.chat.ChatActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;

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
    private static String CHAT_SERVER_URL = "http://dev.dentamatch.co:3000";

    private static Socket mSocket;
    private static SocketManager socketManager;

    private Context mContext;
    private boolean isConnected;


    private SocketManager(Context context) {
        mContext = context;
        getSocket();
    }

    public static SocketManager getInstance(Context context) {
        if (socketManager != null)
            return socketManager;

        return new SocketManager(context);
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
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.connect();
    }

    public void disconnect() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.disconnect();
    }

    public void registerEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
    }

    public void unRegisterEvents() {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
    }

    public void sendMessage(HashMap hashMap) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.emit("sendMsg", new JSONObject(hashMap));
    }

    public void init(HashMap hashMap) {
        if (mSocket == null) {
            LogUtils.LOGD(TAG, "socket is null");
            return;
        }

        mSocket.emit("init", new JSONObject(hashMap));
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, "Socket connected");
            isConnected = true;
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, "Socket disconnected");
            isConnected = false;
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.LOGD(TAG, "Socket connection error");
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.LOGD(TAG, "New message ");

            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            try {
                username = data.getString("username");
                message = data.getString("message");
            } catch (JSONException e) {
                LogUtils.LOGD(TAG, "JSON parse error " + e.getMessage());
            }


        }
    };
}
