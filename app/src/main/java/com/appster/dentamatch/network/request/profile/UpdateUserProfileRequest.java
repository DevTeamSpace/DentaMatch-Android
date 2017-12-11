package com.appster.dentamatch.network.request.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 24/01/17.
 */

public class UpdateUserProfileRequest {
    private String firstName;
    private String lastName;
    private String preferredJobLocationId;
    private String licenseNumber;
    @SerializedName("jobTitileId")
    private int jobTitleID;
    private String aboutMe;
    private String state;


   /* @SerializedName("zipcode")
    private int zipCode;
    private String latitude;
    private String longitude;
    private String preferredJobLocation;
    private String preferredCity;
    private String preferredState;
    private String preferredCountry;
*/


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPreferredJobLocationId() {
        return preferredJobLocationId;
    }

    public void setPreferredJobLocationId(String preferredJobLocationId) {
        this.preferredJobLocationId = preferredJobLocationId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getJobTitleID() {
        return jobTitleID;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /*public void setZipCode(int zipCode) {
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
    }*/

    public void setJobTitleID(int jobTitleID) {
        this.jobTitleID = jobTitleID;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

   /* public void setCity(String city) {
        this.preferredCity = city;
    }

    public void setState(String state) {
        this.preferredState = state;
    }

    public void setCountry(String country) {
        this.preferredCountry = country;
    }*/
}
