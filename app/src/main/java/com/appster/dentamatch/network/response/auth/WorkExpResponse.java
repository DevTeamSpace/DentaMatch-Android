package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 13/01/17.
 */
public class WorkExpResponse extends BaseResponse {


    @SerializedName("result")
    private WorkExpResponseData workExpResponseData;

    public WorkExpResponseData getWorkExpResponseData() {
        return workExpResponseData;
    }
}
