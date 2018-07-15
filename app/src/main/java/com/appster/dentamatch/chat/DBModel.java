package com.appster.dentamatch.chat;

import com.appster.dentamatch.ui.messages.Message;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Appster on 14/02/17.
 * Object model class for DB.
 */

public class DBModel extends RealmObject {

    @Required
    @PrimaryKey
    private String recruiterId;
    private String name;
    private String lastMsgTime;
    private int unReadChatCount;
    private int seekerHasBlocked;
    private String lastMessage;
    private boolean hasDBUpdated;
    private String messageListId;
    private RealmList<Message> userChats;

    public String getMessageListId() {
        return messageListId;
    }

    public void setMessageListId(String messageListId) {
        this.messageListId = messageListId;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public boolean isDBUpdated() {
        return hasDBUpdated;
    }

    public void setHasDBUpdated(boolean hasDBUpdated) {
        this.hasDBUpdated = hasDBUpdated;
    }

    public int getUnReadChatCount() {
        return unReadChatCount;
    }

    public void setUnReadChatCount(int unReadChatCount) {
        this.unReadChatCount = unReadChatCount;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeekerHasBlocked() {
        return seekerHasBlocked;
    }

    public void setSeekerHasBlocked(int seekerBlock) {
        this.seekerHasBlocked = seekerBlock;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public RealmList<Message> getUserChats() {
        return userChats;
    }

}
