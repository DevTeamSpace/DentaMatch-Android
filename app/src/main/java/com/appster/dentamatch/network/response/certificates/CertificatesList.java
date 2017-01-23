package com.appster.dentamatch.network.response.certificates;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/01/17.
 */
public class CertificatesList {
    private int id;
    private String certificateName;
    @SerializedName("imagePath")
    private String image;
    private String imageUrl;
    @SerializedName("validityDate")
    private String validityDate;
    private boolean isImageUploaded;

    public boolean isImageUploaded() {
        return isImageUploaded;
    }

    public void setImageUploaded(boolean imageUploaded) {
        isImageUploaded = imageUploaded;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
