/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.calendar;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 08/02/17.
 * To inject activity reference.
 */
public class AvailabilityResponse extends BaseResponse {
    @SerializedName("result")
    private AvailabilityResponseData availabilityResponseData;

    public AvailabilityResponseData getAvailabilityResponseData() {
        return availabilityResponseData;
    }
}
