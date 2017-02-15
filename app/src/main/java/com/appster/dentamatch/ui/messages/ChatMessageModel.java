package com.appster.dentamatch.ui.messages;

import io.realm.RealmObject;

/**
 * Created by Appster on 13/02/17.
 */

public class ChatMessageModel extends RealmObject {
    private String message;
    private String fromID;
    private String toID;
    private String messageTime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
