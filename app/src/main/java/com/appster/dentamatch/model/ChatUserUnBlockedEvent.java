package com.appster.dentamatch.model;

/**
 * Created by Appster on 09/02/17.
 */

public class ChatUserUnBlockedEvent {
    private int recruiterID;

    public  ChatUserUnBlockedEvent(int recruiterID){
        this.recruiterID = recruiterID;
    }

    public int getRecruiterID() {
        return recruiterID;
    }
}
