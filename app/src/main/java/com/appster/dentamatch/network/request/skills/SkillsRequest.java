package com.appster.dentamatch.network.request.skills;

/**
 * Model class to hold Login request data.
 */
public class SkillsRequest {
    private String email;
    private String password;
    private String deviceId;
    private String deviceType;
    private String deviceToken;
    private String firstName;
    private String lastName;
    private String preferedLocation;
    private String latitude;
    private String longitude;

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPreferedLocation(String preferedLocation) {
        this.preferedLocation = preferedLocation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String zipCode;


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
}
