/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.fileupload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 12/01/17.
 * To inject activity reference.
 */
public class FileUploadResponseData {
    @SerializedName("img_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
