package com.appster.dentamatch.network.response.affiliation;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 16/01/17.
 */
public class AffiliationResponse extends BaseResponse {
    @SerializedName("result")
    private AffiliationResponseData affiliationResponseData;

    public AffiliationResponseData getAffiliationResponseData() {
        return affiliationResponseData;
    }



}
