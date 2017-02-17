package com.appster.dentamatch.network.response.affiliation;

import com.appster.dentamatch.network.request.affiliation.Affiliation;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 */
public class AffiliationResponseData {

    @SerializedName("list")
    private ArrayList<Affiliation> affiliationList;

    public ArrayList<Affiliation> getAffiliationList() {
        return affiliationList;
    }

}
