package com.appster.dentamatch.network.response.workexp;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class WorkExpResponse extends BaseResponse {


    @SerializedName("result")
    private WorkExpResponseData workExpResponseData;


//    @SerializedName("workExperience")
//    private WorkExpResponseData workExpericenceProfile;
//
//    public WorkExpResponseData getWorkExpericenceProfile() {
//        return workExpericenceProfile;
//    }

    public WorkExpResponseData getWorkExpResponseData() {
        return workExpResponseData;
    }
}
