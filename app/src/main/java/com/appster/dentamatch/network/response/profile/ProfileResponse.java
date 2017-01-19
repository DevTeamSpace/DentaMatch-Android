package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 19/01/17.
 */
public class ProfileResponse extends BaseResponse {
    @SerializedName("result")
    private ProfileResponseData profileResponseData;
}
