package com.appster.dentamatch.network.request.affiliation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 * To inject activity reference.
 */
public class AffiliationPostRequest {
    @SerializedName("affiliationDataArray")
    private ArrayList<Integer> idList;

    @SerializedName("other")
    private ArrayList<OtherAffiliationRequest> otherAffiliationList;

    public void setIdList(ArrayList<Integer> idList) {
        this.idList = idList;
    }

    public void setOtherAffiliationList(ArrayList<OtherAffiliationRequest> otherAffiliationList) {
        this.otherAffiliationList = otherAffiliationList;
    }
}
