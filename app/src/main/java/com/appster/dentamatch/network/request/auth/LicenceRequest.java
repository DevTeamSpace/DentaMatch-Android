package com.appster.dentamatch.network.request.auth;

/**
 * Created by virender on 12/01/17.
 */
public class LicenceRequest {

    private String license;
    private String state;
    private int jobTitleId;




    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
}


