package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ramkumar on 16/01/17.
 * To inject activity reference.
 */

public class SchoolModel {
    @SerializedName("schoolingChildId")
    private int schoolId;
    @SerializedName("schoolChildName")
    private String schoolName;
    @SerializedName("schoolingId")
    private int schoolTypeId;
    @SerializedName("jobSeekerStatus")
    private int isSelected;
    private String yearOfGraduation;
    private String otherSchooling;

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

    public int getSchoolTypeId() {
        return schoolTypeId;
    }

    public void setSchoolTypeId(int schoolTypeId) {
        this.schoolTypeId = schoolTypeId;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
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
