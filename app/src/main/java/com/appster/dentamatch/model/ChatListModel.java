package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Appster on 08/02/17.
 */

public class ChatListModel implements Parcelable{
    private int messageListId;
    private int seekerId;
    private int messageId;
    private String timestamp;
    private String message;
    private int recruiterId;
    private int recruiterBlock;
    private String name;
    private int seekerBlock;


    protected ChatListModel(Parcel in) {
        messageListId = in.readInt();
        seekerId = in.readInt();
        messageId = in.readInt();
        timestamp = in.readString();
        message = in.readString();
        recruiterId = in.readInt();
        recruiterBlock = in.readInt();
        name = in.readString();
        seekerBlock = in.readInt();
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
        this.seekerBlock = seekerBlock;
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

    public String getName() {
        return name;
    }

    public int getSeekerBlock() {
        return seekerBlock;
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
        dest.writeInt(seekerBlock);
    }
}
