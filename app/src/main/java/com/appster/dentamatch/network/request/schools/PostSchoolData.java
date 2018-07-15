/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.schools;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 25/01/17.
 * To inject activity reference.
 */
public class PostSchoolData {

    @SerializedName("schoolingChildId")
    private int schoolId;
    @SerializedName("schoolChildName")
    private String schoolName;
    private String yearOfGraduation;

    private String otherSchooling;
    private String otherId;

    private String parentSchoolName;

    public String getParentSchoolName() {
        return parentSchoolName;
    }

    public void setParentSchoolName(String parentSchoolName) {
        this.parentSchoolName = parentSchoolName;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public String getYearOfGraduation() {
        return yearOfGraduation;
    }

    public void setYearOfGraduation(String yearOfGraduation) {
        this.yearOfGraduation = yearOfGraduation;
    }

    public String getOtherSchooling() {
        return otherSchooling;
    }

    public void setOtherSchooling(String otherSchooling) {
        this.otherSchooling = otherSchooling;
    }
}


