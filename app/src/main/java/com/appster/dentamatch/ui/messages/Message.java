package com.appster.dentamatch.ui.messages;

import io.realm.RealmObject;

public class Message extends RealmObject{

    public static final int TYPE_MESSAGE_SEND = 0;
    public static final int TYPE_DATE_HEADER = 1;
    public static final int TYPE_MESSAGE_RECEIVED = 3;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mMessageTime;
    private String mMessageId;

    public Message(){

    }

    public Message(String mMessage, String mUsername, String mMessageTime, String mMessageId, int mType) {
        this.mMessage = mMessage;
        this.mUsername = mUsername;
        this.mMessageTime = mMessageTime;
        this.mMessageId = mMessageId;
        this.mType = mType;

    }

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getmMessageTime() {
        return mMessageTime;
    }

    public String getmMessageId() {
        return mMessageId;
    }


}
