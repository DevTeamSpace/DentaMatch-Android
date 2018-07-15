package com.appster.dentamatch.network.request.jobs;

/**
 * Created by virender on 09/02/17.
 * To inject activity reference.
 */
public class HiredJobRequest {

    private String jobStartDate;
    private String jobEndDate;


    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public void setJobEndDate(String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }
}
