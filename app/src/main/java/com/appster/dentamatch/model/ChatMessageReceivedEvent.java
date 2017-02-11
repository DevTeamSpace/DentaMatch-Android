package com.appster.dentamatch.model;

/**
 * Created by Appster on 09/02/17.
 */

public class ChatMessageReceivedEvent {
    private String fromId, toId, message, sentTime;

    public ChatMessageReceivedEvent(String fromId, String toId, String message, String sentTime){
        this.fromId = fromId;
        this.toId = toId;
        this.sentTime = sentTime;
        this.message = message;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentTime() {
        return sentTime;
    }



}
