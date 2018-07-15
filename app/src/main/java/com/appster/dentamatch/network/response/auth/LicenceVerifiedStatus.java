/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.model.UserModel;

/**
 * Created by abhaykant on 08/12/17.
 * To inject activity reference.
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
