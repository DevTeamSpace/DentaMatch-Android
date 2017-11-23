package com.appster.dentamatch.network.request.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Model class to hold Login request data.
 */
public class LoginRequest {
    private String email;
    private String password;
    private String deviceId;
    private String deviceType;
    private String deviceToken;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    @SerializedName("preferedLocation")
    private String preferredLocation;
    private String latitude;
    private String longitude;
    private String zipCode;
    private int preferredJobLocationId;

    public int getPreferredJobLocationId() {
        return preferredJobLocationId;
    }

    public void setPreferredJobLocationId(int preferredJobLocationId) {
        this.preferredJobLocationId = preferredJobLocationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
