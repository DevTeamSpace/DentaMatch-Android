/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 * To inject activity reference.
 */
public class JobTitleResponse extends BaseResponse {

    @SerializedName("result")
    private JobTitleResponseData jobTitleResponseData;

    public JobTitleResponseData getJobTitleResponseData() {
        return jobTitleResponseData;
    }
}