package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleResponse extends BaseResponse {

    @SerializedName("result")
    private JobTitleResponseData jobTitleResponseData;

    public JobTitleResponseData getJobTitleResponseData() {
        return jobTitleResponseData;
    }
}