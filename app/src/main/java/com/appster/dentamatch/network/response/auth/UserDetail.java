package com.appster.dentamatch.network.response.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ram on 06/01/17.
 */

public class UserDetail {
    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private String email;
    private String zipCode;
    private String preferredJobLocation;
    private int profileCompleted;

    public String getImageUrl() {
        return imageUrl;
    }
    public int getProfileCompleted() {
        return profileCompleted;
    }

    public String getId() {
        return id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    @SerializedName("accessToken")
    private String userToken;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPreferredJobLocation() {
        return preferredJobLocation;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPreferredJobLocation(String preferredJobLocation) {
        this.preferredJobLocation = preferredJobLocation;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
