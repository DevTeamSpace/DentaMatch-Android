package com.appster.dentamatch.network.request.auth;

/**
 * Created by virender on 12/01/17.
 */
public class LicenceRequest {

    private String license;
    private String state;
    private String jobTitleId;


    public void setJobTitleId(String jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setState(String state) {
        this.state = state;
    }




}


