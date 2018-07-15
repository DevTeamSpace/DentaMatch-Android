/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 02/02/17.
 * To inject activity reference.
 */

public class ChangeUserLocation {
    @SerializedName("preferedLocation")
    private String preferredLocation;
    private String preferredCity;
    private String preferredState;
    private String preferredCountry;
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

    public void setPreferredCity(String preferredCity) {
        this.preferredCity = preferredCity;
    }

    public void setPreferredState(String preferredState) {
        this.preferredState = preferredState;
    }

    public void setPreferredCountry(String preferredCountry) {
        this.preferredCountry = preferredCountry;
    }
}
