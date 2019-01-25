/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.jobs;

import com.appster.dentamatch.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 10/02/17.
 * To inject activity reference.
 */
public class HiredJobResponse extends BaseResponse {
    @SerializedName("result")

    private HiredJobResponseData hiredJobResponseData;

    public HiredJobResponseData getHiredJobResponseData() {
        return hiredJobResponseData;
    }

}
