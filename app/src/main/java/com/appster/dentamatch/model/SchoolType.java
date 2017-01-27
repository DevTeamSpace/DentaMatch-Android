package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ramkumar on 16/01/17.
 */

public class SchoolType {
    @SerializedName("schoolingId")
    private int schoolTypeId;
    @SerializedName("schoolName")
    private String schoolTypeName;
    @SerializedName("schoolCategory")
    private ArrayList<School> schoolList;
    @SerializedName("other")
    private ArrayList<School> otherList;

    public ArrayList<School> getOtherList() {
        return otherList;
    }

    public void setOtherList(ArrayList<School> otherList) {
        this.otherList = otherList;
    }

    public int getSchoolTypeId() {
        return schoolTypeId;
    }

    public void setSchoolTypeId(int schoolTypeId) {
        this.schoolTypeId = schoolTypeId;
    }

    public String getSchoolTypeName() {
        return schoolTypeName;
    }

    public void setSchoolTypeName(String schoolTypeName) {
        this.schoolTypeName = schoolTypeName;
    }

    public ArrayList<School> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(ArrayList<School> schoolList) {
        this.schoolList = schoolList;
    }
}
