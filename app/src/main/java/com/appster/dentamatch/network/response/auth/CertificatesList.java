package com.appster.dentamatch.network.response.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/01/17.
 */
public class CertificatesList {
    private int id;
    private String certificateName;
    private String image;
    @SerializedName("validity_date")
    private String validityDate;
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


}
