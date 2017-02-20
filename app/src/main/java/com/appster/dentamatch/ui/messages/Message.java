package com.appster.dentamatch.ui.messages;

public class Message {

    public static final int TYPE_MESSAGE_SEND = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_RECEIVED = 3;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mMessageTime;


    public Message() {}

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

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mMessageTime;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder time(String time) {
            mMessageTime = time;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mMessageTime = mMessageTime;
            return message;
        }
    }
}
