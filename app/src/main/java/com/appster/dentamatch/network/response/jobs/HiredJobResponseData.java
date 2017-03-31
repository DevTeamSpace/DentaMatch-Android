package com.appster.dentamatch.network.response.jobs;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 10/02/17.
 */
public class HiredJobResponseData {
    @SerializedName("list")
    private ArrayList<HiredJobs> jobList;


    public ArrayList<HiredJobs> getJobList() {
        return jobList;
    }
}
