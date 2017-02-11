package com.appster.dentamatch.chat;

import io.realm.RealmModel;

/**
 * Created by ramkumar on 09/02/17.
 */

public class ChatMessage implements RealmModel{
    private String msgId;
    private long msgTimeStamp;
    private String msgText;
    private int msgStatus; //sent (0), delivered (1), read (2)
    private String chatId;
    private String chatThreadId;
    private int msgType; // sender (1), receiver (2)
    private boolean readStatusSent;
    private String toChatId;
    private String fromChatId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getMsgTimeStamp() {
        return msgTimeStamp;
    }

    public void setMsgTimeStamp(long msgTimeStamp) {
        this.msgTimeStamp = msgTimeStamp;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatThreadId() {
        return chatThreadId;
    }

    public void setChatThreadId(String chatThreadId) {
        this.chatThreadId = chatThreadId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isReadStatusSent() {
        return readStatusSent;
    }

    public void setReadStatusSent(boolean readStatusSent) {
        this.readStatusSent = readStatusSent;
    }

    public String getToChatId() {
        return toChatId;
    }

    public void setToChatId(String toChatId) {
        this.toChatId = toChatId;
    }

    public String getFromChatId() {
        return fromChatId;
    }

    public void setFromChatId(String fromChatId) {
        this.fromChatId = fromChatId;
    }
}
