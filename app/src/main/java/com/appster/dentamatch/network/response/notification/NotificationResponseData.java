/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.notification;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 14/02/17.
 * To inject activity reference.
 */
public class NotificationResponseData {
    private int total;
    @SerializedName("list")
    private ArrayList<NotificationData> notificationList;

    public int getTotal() {
        return total;
    }

    public ArrayList<NotificationData> getNotificationList() {
        return notificationList;
    }
}
