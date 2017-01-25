package com.appster.dentamatch.network.response.schools;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Response class for Schooling and Certification.
 */
public class SchoolingResponse extends BaseResponse {

    @SerializedName("result")
    private SchoolingResponseData schoolingResponseData;

    public SchoolingResponseData getSchoolingResponseData() {
        return schoolingResponseData;
    }

    public void setSchoolingResponseData(SchoolingResponseData schoolingResponseData) {
        this.schoolingResponseData = schoolingResponseData;
    }
}