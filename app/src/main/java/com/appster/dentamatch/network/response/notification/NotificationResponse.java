package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.response.jobs.HiredJobResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/02/17.
 */
public class NotificationResponse extends BaseResponse {
    @SerializedName("result")

    private NotificationResponseData NotificationResponseData;

    public NotificationResponseData getNotificationResponseData() {
        return NotificationResponseData;
    }

}