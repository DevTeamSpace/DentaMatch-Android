package com.appster.dentamatch.network.response.certificates;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/01/17.
 */
public class CertificateResponse extends BaseResponse {

    @SerializedName("result")
    private CertificateResponseData certificateResponseData;

    public CertificateResponseData getCertificateResponseData() {
        return certificateResponseData;
    }

}
