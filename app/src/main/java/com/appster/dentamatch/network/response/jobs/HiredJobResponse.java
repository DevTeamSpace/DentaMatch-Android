package com.appster.dentamatch.network.response.jobs;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 10/02/17.
 */
public class HiredJobResponse extends BaseResponse {
    @SerializedName("result")

    private HiredJobResponseData hiredJobResponseData;

    public HiredJobResponseData getHiredJobResponseData() {
        return hiredJobResponseData;
    }

}
