package com.appster.dentamatch.network.response.affiliation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 */
public class AffiliationResponseData {

    @SerializedName("list")
    private ArrayList<AffiliationData> affiliationList;

    public ArrayList<AffiliationData> getAffiliationList() {
        return affiliationList;
    }

}
