package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 08/02/17.
 * To inject activity reference.
 */

public class ChatListModel implements Parcelable{
    private final int messageListId;
    private final int seekerId;
    private final int messageId;
    private final String timestamp;
    private final String message;
    private final int recruiterId;
    private int recruiterBlock;
    private final String name;
    private final String unreadCount;
    @SerializedName("seekerBlock")
    private int seekerHasBlocked;


    private ChatListModel(Parcel in) {
        messageListId = in.readInt();
        seekerId = in.readInt();
        messageId = in.readInt();
        timestamp = in.readString();
        unreadCount = in.readString();
        message = in.readString();
        recruiterId = in.readInt();
        recruiterBlock = in.readInt();
        name = in.readString();
        seekerHasBlocked = in.readInt();
    }

    public static final Creator<ChatListModel> CREATOR = new Creator<ChatListModel>() {
        @Override
        public ChatListModel createFromParcel(Parcel in) {
            return new ChatListModel(in);
        }

        @Override
        public ChatListModel[] newArray(int size) {
            return new ChatListModel[size];
        }
    };

    public void setRecruiterBlock(int recruiterBlock) {
        this.recruiterBlock = recruiterBlock;
    }

    public void setSeekerBlock(int seekerBlock) {
        this.seekerHasBlocked = seekerBlock;
    }

    public int getMessageListId() {
        return messageListId;
    }

    public int getSeekerId() {
        return seekerId;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public int getRecruiterBlock() {
        return recruiterBlock;
    }

    public String getUnreadCount() {
        return unreadCount;
    }
    public String getName() {
        return name;
    }

    public int getSeekerHasBlocked() {
        return seekerHasBlocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(messageListId);
        dest.writeInt(seekerId);
        dest.writeInt(messageId);
        dest.writeString(timestamp);
        dest.writeString(message);
        dest.writeInt(recruiterId);
        dest.writeInt(recruiterBlock);
        dest.writeString(name);
        dest.writeString(unreadCount);
        dest.writeInt(seekerHasBlocked);
    }
}
