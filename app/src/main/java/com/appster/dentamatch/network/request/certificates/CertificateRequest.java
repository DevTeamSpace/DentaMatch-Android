package com.appster.dentamatch.network.request.certificates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 */
public class CertificateRequest {

    @SerializedName("certificateValidition")
    ArrayList<UpdateCertificates> updateCertificatesList;

    public void setUpdateCertificatesList(ArrayList<UpdateCertificates> updateCertificatesList) {
        this.updateCertificatesList = updateCertificatesList;
    }


}
