package com.appster.dentamatch.network.request.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 02/02/17.
 */

public class ChangeUserLocation {
    @SerializedName("preferedLocation")
    private String preferredLocation;
    private String latitude;
    private String longitude;
    private int zipCode;

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
