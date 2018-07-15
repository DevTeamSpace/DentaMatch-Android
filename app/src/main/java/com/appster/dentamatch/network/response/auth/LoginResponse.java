/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Class to hold Login response.
 */
public class LoginResponse extends BaseResponse {

    @SerializedName("result")
    private LoginResponseData loginResponseData;

    public LoginResponseData getLoginResponseData() {
        return loginResponseData;
    }

    public void setLoginResponseData(LoginResponseData loginResponseData) {
        this.loginResponseData = loginResponseData;
    }
}