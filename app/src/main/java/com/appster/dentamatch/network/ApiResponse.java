package com.appster.dentamatch.network;

import com.appster.dentamatch.model.response.SignInResponse;
import com.appster.dentamatch.model.response.SignUpResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Appster on 23/05/16.
 */
public class ApiResponse {

    public boolean status;
    public String message;
    public SignInResponse result;
    public boolean noContentFound;

    @SerializedName("user")
    public SignUpResponse signUpResponse;
    public String otp;
    public String accessToken;
    public String page;
}

