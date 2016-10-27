package com.appster.dentamatch.model.request;

/**
 * Created by Appster on 23/05/16.
 */
public class SignUpRequest {
    public String email;
    public String password;
    public String name;
    public String deviceId;
    public int roleType ;
    public int deviceType = 2;
    public String deviceToken;
    public String latitude;
    public String longitude;
}
