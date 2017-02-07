package com.appster.dentamatch.chat;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by ramkumar on 06/02/17.
 */

public class SocketManager {
    private static Socket mSocket;
    private static String CHAT_SERVER_URL = "http://dev.dentamatch.co:3000";

    public static Socket getSocket() {
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return mSocket;
    }
}
