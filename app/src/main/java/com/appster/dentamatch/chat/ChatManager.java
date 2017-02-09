package com.appster.dentamatch.chat;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.model.SocketConnectionEvent;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Appster on 08/02/17.
 */
public class ChatManager {
    private static ChatManager ourInstance;
    private Socket mSocket;

    public synchronized static ChatManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ChatManager();
        }
        return ourInstance;
    }


    private ChatManager() {

    }

    /**
     * Initialising socket class and connect .
     */
    public void init() {
        mSocket = DentaApp.getInstance().getSocket();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.connect();
    }

    private void initialiseUser() {
        User user = PreferenceUtil.getUserModel();

        if (user.getId() != 0) {
            HashMap<String, String> userMap = new HashMap<>();
            userMap.put("userId", "606");
            mSocket.emit("init", new JSONObject(userMap));
        }
    }

    /**
     * We use eventBus to fire the event on connected.
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            EventBus.getDefault().post(new SocketConnectionEvent(Constants.CONNECTED));
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            EventBus.getDefault().post(new SocketConnectionEvent(Constants.DISCONNECTED));
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };


}
