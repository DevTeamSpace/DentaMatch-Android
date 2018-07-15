package com.appster.dentamatch.network.request.Notification;

/**
 * Created by virender on 22/02/17.
 * To inject activity reference.
 */
public class UpdateFcmTokenRequest {
    private String updateDeviceToken;


    public void setUpdateDeviceToken(String updateDeviceToken) {
        this.updateDeviceToken = updateDeviceToken;
    }
}
