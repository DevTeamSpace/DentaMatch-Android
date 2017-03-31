package com.appster.dentamatch.eventbus;

/**
 * Created by bawenderyandra on 10/03/17.
 */

public class UnblockEvent {
    private boolean status;

    public UnblockEvent(boolean status){
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
