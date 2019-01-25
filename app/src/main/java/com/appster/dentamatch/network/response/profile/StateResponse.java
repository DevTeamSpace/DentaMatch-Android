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
 * Created by atul on 14/11/18.
 * To inject activity reference.
 */
public class StateResponse extends BaseResponse {

    @SerializedName("result")
    private StateResponseData result;

    public StateResponseData getResult() {
        return result;
    }
}
