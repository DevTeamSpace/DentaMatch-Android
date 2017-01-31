package com.appster.dentamatch.network.response.fileupload;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 */
public class FileUploadResponse extends BaseResponse {

    @SerializedName("result")
    private FileUploadResponseData fileUploadResponseData;

    public FileUploadResponseData getFileUploadResponseData() {
        return fileUploadResponseData;
    }

}
