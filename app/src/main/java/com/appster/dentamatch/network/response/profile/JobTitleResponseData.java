package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.model.JobTitleList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleResponseData {
    @SerializedName("joblists")
    private ArrayList<JobTitleList> jobTitleList;

    public ArrayList<JobTitleList> getJobTitleList() {
        return jobTitleList;
    }
}
