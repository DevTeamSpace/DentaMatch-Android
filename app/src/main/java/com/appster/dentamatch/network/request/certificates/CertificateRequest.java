package com.appster.dentamatch.network.request.certificates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 * To inject activity reference.
 */
public class CertificateRequest {

    @SerializedName("certificateValidition")
    private ArrayList<UpdateCertificates> updateCertificatesList;

    public void setUpdateCertificatesList(ArrayList<UpdateCertificates> updateCertificatesList) {
        this.updateCertificatesList = updateCertificatesList;
    }


}
