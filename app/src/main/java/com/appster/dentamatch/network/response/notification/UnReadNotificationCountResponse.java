/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.notification;

import com.appster.dentamatch.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 28/02/17.
 * To inject activity reference.
 */
public class UnReadNotificationCountResponse extends BaseResponse {
    @SerializedName("result")
    private UnReadNotificationResponseData unReadNotificationResponse;

    public UnReadNotificationResponseData getUnReadNotificationResponse() {
        return unReadNotificationResponse;
    }

    public void setUnReadNotificationResponse(UnReadNotificationResponseData unReadNotificationResponse) {
        this.unReadNotificationResponse = unReadNotificationResponse;
    }
}
