/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.fileupload;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 * To inject activity reference.
 */
public class FileUploadResponse extends BaseResponse {

    @SerializedName("result")
    private FileUploadResponseData fileUploadResponseData;

    public FileUploadResponseData getFileUploadResponseData() {
        return fileUploadResponseData;
    }

}
