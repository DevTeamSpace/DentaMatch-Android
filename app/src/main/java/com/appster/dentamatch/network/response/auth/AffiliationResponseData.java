package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.network.request.auth.AffiliationRequest;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 */
public class AffiliationResponseData {

    @SerializedName("list")
    private ArrayList<AffiliationRequest> affiliationList;

    public ArrayList<AffiliationRequest> getAffiliationList() {
        return affiliationList;
    }

}
