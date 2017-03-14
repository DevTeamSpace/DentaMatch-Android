package com.appster.dentamatch.network.response.affiliation;

import com.appster.dentamatch.EventBus.LocationEvent;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 16/01/17.
 */
public class AffiliationResponseData {

    @SerializedName("list")
    private ArrayList<LocationEvent.Affiliation> affiliationList;

    public ArrayList<LocationEvent.Affiliation> getAffiliationList() {
        return affiliationList;
    }

}
