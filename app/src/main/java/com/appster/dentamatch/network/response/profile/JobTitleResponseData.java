package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.model.JobTitleListModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleResponseData {
    @SerializedName("joblists")
    private ArrayList<JobTitleListModel> jobTitleList;

    public ArrayList<JobTitleListModel> getJobTitleList() {
        return jobTitleList;
    }
}
