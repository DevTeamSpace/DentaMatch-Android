package com.appster.dentamatch.model;

/**
 * Created by Appster on 08/02/17.
 */

public class SocketConnectionEvent {
    private String status;

    public SocketConnectionEvent(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
