package com.appster.dentamatch.RealmDataBase;

import com.appster.dentamatch.ui.messages.Message;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Appster on 14/02/17.
 */

public class DBModel extends RealmObject {
    @PrimaryKey
    private String recruiterId;
    private String name;
    private int unReadChatCount;
    private int seekerHasBlocked;
    private String lastMessage;
    private boolean hasDBUpdated;

    public boolean isDBUpdated() {
        return hasDBUpdated;
    }

    public void setHasDBUpdated(boolean hasDBUpdated) {
        this.hasDBUpdated = hasDBUpdated;
    }

    private RealmList<Message> userChats;

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
