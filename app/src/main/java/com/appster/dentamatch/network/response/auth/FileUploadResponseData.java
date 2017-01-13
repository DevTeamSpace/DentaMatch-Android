package com.appster.dentamatch.network.response.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 */
public class FileUploadResponseData {
    @SerializedName("img_url")
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
