package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.model.JobDetailModel;

/**
 * Created by virender on 14/02/17.
 */
public class NotificationData {

    private int senderId;
    private int receiverId;
    private int id;


    private int seen;
    private int jobListId;
    private int notificationType;
    private String createdAt;
    private String notificationData;
    private JobDetailModel jobDetails;
    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }
    public int getnotificationType() {
        return notificationType;
    }

    public int getId() {
        return id;
    }

    public int getSeen() {
        return seen;
    }

    public int getJobListId() {
        return jobListId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNotificationData() {
        return notificationData;
    }

    public JobDetailModel getJobDetailModel() {
        return jobDetails;
    }
}
