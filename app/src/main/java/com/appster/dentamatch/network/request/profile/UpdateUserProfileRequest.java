package com.appster.dentamatch.network.request.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 24/01/17.
 */

public class UpdateUserProfileRequest {
    private String firstName;
    private String lastName;

    @SerializedName("zipcode")
    private int zipCode;
    private String latitude;
    private String longitude;
    private String preferredJobLocation;

    @SerializedName("jobTitileId")
    private int jobTitleID;
    private String aboutMe;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPreferredJobLocation(String preferredJobLocation) {
        this.preferredJobLocation = preferredJobLocation;
    }

    public void setJobTitleID(int jobTitleID) {
        this.jobTitleID = jobTitleID;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

}
