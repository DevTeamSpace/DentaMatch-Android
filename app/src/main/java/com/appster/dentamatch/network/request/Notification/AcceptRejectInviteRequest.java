package com.appster.dentamatch.network.request.Notification;

/**
 * Created by virender on 27/02/17.
 */
public class AcceptRejectInviteRequest {
    private int notificationId;
    private int acceptStatus;


    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public void setAcceptStatus(int acceptStatus) {
        this.acceptStatus = acceptStatus;
    }
}
