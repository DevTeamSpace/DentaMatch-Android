package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.model.UserModel;

/**
 * Created by abhaykant on 08/12/17.
 */

public class LicenceVerifiedStatus {
    public UserModel getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserModel userDetails) {
        this.userDetails = userDetails;
    }

    private UserModel userDetails;



}
