package com.appster.dentamatch.network.response.calendar;

import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.response.certificates.CertificateResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 08/02/17.
 */
public class AvailabilityResponse extends BaseResponse {
    @SerializedName("result")
    private AvailabilityResponseData availabilityResponseData;

    public AvailabilityResponseData getAvailabilityResponseData() {
        return availabilityResponseData;
    }
}
