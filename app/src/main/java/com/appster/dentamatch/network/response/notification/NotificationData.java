/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.model.JobDetailModel;

import java.util.ArrayList;

/**
 * Created by virender on 14/02/17.
 * To inject activity reference.
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
    private ArrayList<String> currentAvailability;

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getNotificationType() {
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

    public ArrayList<String> getCurrentAvailability() {
        return currentAvailability;
    }
}
