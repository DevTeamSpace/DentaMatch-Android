/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.affiliation;

import com.appster.dentamatch.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 16/01/17.
 * To inject activity reference.
 */
public class AffiliationResponse extends BaseResponse {
    @SerializedName("result")
    private AffiliationResponseData affiliationResponseData;

    public AffiliationResponseData getAffiliationResponseData() {
        return affiliationResponseData;
    }



}
