package com.appster.dentamatch.chat;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ramkumar on 09/02/17.
 */

public class ChatThread implements RealmModel{
    private String chatId;
    @PrimaryKey
    private String chatThreadId;
    private boolean isBlockedBySeeker;
    private boolean isBlockedByRecruiter;
    private ChatMessage latestChatMsg;
    private long latestMsgTimeStamp;
    private String recruiterId;
    private String officeName;
    private RealmList<ChatMessage> chatMessages;

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

    public boolean isBlockedBySeeker() {
        return isBlockedBySeeker;
    }

    public void setBlockedBySeeker(boolean blockedBySeeker) {
        isBlockedBySeeker = blockedBySeeker;
    }

    public boolean isBlockedByRecruiter() {
        return isBlockedByRecruiter;
    }

    public void setBlockedByRecruiter(boolean blockedByRecruiter) {
        isBlockedByRecruiter = blockedByRecruiter;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public ChatMessage getLatestChatMsg() {
        return latestChatMsg;
    }

    public void setLatestChatMsg(ChatMessage latestChatMsg) {
        this.latestChatMsg = latestChatMsg;
    }

    public long getLatestMsgTimeStamp() {
        return latestMsgTimeStamp;
    }

    public void setLatestMsgTimeStamp(long latestMsgTimeStamp) {
        this.latestMsgTimeStamp = latestMsgTimeStamp;
    }

    public RealmList<ChatMessage> getMessages() {
        return chatMessages;
    }

    public void setMessages(RealmList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
