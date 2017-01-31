package com.appster.dentamatch.network.request.schools;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ramkumar on 17/01/17.
 */

public class AddSchoolRequest {
    @SerializedName("schoolDataArray")
    private ArrayList<PostSchoolData> schoolingData;

    public ArrayList<PostSchoolData> getSchoolingData() {
        return schoolingData;
    }

    public void setSchoolingData(ArrayList<PostSchoolData> schoolingData) {
        this.schoolingData = schoolingData;
    }
}
