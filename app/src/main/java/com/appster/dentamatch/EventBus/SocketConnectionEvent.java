package com.appster.dentamatch.eventbus;

/**
 * Created by Appster on 08/02/17.
 */

public class SocketConnectionEvent {
    private boolean connectionStatus;

    public SocketConnectionEvent(boolean status){
        this.connectionStatus = status;
    }

    public boolean getStatus() {
        return connectionStatus;
    }
}
