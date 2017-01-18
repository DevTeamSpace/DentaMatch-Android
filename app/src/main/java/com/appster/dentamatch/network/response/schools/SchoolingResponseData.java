package com.appster.dentamatch.network.response.schools;

import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SchoolType;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold Schooling response data.
 */
public class SchoolingResponseData {

    @SerializedName("list")
    private ArrayList<SchoolType> schoolTypeList;

    public List<SchoolType> getSchoolTypeList() {
        return schoolTypeList;
    }
}
