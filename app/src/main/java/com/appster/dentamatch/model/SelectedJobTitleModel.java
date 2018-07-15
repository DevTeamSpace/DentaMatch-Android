package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bawenderyandra on 24/03/17.
 * To inject activity reference.
 */

public class SelectedJobTitleModel {
    @SerializedName("jobId")
    private int id;

    @SerializedName("jobtitleName")
    private String jobTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
