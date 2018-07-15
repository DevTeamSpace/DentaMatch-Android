/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/02/17.
 * To inject activity reference.
 */
public class NotificationResponse extends BaseResponse {
    @SerializedName("result")

    private NotificationResponseData NotificationResponseData;

    public NotificationResponseData getNotificationResponseData() {
        return NotificationResponseData;
    }

}