package com.appster.dentamatch.network.response.certificates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 14/01/17.
 */
public class CertificateResponseData {
    @SerializedName("list")

    private ArrayList<CertificatesList>certificatesLists;
    public ArrayList<CertificatesList> getCertificatesLists() {
        return certificatesLists;
    }


}
