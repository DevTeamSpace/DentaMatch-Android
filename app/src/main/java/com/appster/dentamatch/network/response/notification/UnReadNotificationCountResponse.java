package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 28/02/17.
 */
public class UnReadNotificationCountResponse extends BaseResponse {
    @SerializedName("result")
    private UnReadNotificationResponseData unReadNotificationResponse;

    public UnReadNotificationResponseData getUnReadNotificationResponse() {
        return unReadNotificationResponse;
    }
}