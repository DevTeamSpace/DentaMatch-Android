package com.appster.dentamatch.model;

/**
 * Created by Appster on 08/02/17.
 */

public class ChatHistoryModel {
    private int messageListId;
    private int seekerId;
    private int messageId;
    private String timestamp;
    private String message;
    private int recruiterId;
    private int recruiterBlock;
    private String name;
    private int seekerBlock;

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
}
