package com.appster.dentamatch.network.response.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleList {


    private int id;
    @SerializedName("jobtitle_name")
    private String jobTitle;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

}
