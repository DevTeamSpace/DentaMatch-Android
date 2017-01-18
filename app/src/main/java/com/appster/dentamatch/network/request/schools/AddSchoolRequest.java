package com.appster.dentamatch.network.request.schools;

import com.appster.dentamatch.model.SchoolType;

import java.util.ArrayList;

/**
 * Created by ramkumar on 17/01/17.
 */

public class AddSchoolRequest {
    private ArrayList<SchoolType> schoolingData;

    public ArrayList<SchoolType> getSchoolingData() {
        return schoolingData;
    }

    public void setSchoolingData(ArrayList<SchoolType> schoolingData) {
        this.schoolingData = schoolingData;
    }
}
