package com.appster.dentamatch.network.response.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleList {
    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    private int id;
    @SerializedName("jobtitle_name")
    private String jobTitle;

}