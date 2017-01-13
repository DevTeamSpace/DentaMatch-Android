package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 */
public class FileUploadResponse extends BaseResponse {

    @SerializedName("result")
    FileUploadResponseData fileUploadResponseData;

    public FileUploadResponseData getFileUploadResponseData() {
        return fileUploadResponseData;
    }

}
