/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.schools;

import com.appster.dentamatch.model.SchoolTypeModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold Schooling response data.
 */
public class SchoolingResponseData {

    @SerializedName("list")
    private ArrayList<SchoolTypeModel> schoolTypeList;

    public List<SchoolTypeModel> getSchoolTypeList() {
        return schoolTypeList;
    }
}
