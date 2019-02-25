/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.workexp;

import com.appster.dentamatch.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class WorkExpResponse extends BaseResponse {

    @SerializedName("result")
    private WorkExpResponseData workExpResponseData;

    public WorkExpResponseData getWorkExpResponseData() {
        return workExpResponseData;
    }
}
