package com.appster.dentamatch.network.request.auth;

/**
 *
 */
public class LoginRequest {

    public String mEmailId;
    public String mPassword;
    public String mDeviceType;

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String emailId) {
        this.mEmailId = emailId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(String deviceType) {
        this.mDeviceType = deviceType;
    }
}
