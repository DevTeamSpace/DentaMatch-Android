package com.appster.dentamatch.network.response.fileupload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 */
public class FileUploadResponseData {
    @SerializedName("img_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
