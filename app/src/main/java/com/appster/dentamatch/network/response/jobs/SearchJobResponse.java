package com.appster.dentamatch.network.response.jobs;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 27/01/17.
 * To inject activity reference.
 */

public class SearchJobResponse extends BaseResponse {
    @SerializedName("result")
    private SearchJobResponseData searchJobResponseData;

    public SearchJobResponseData getSearchJobResponseData() {
        return searchJobResponseData;
    }
}
