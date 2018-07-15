package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ramkumar on 16/01/17.
 * To inject activity reference.
 */

public class SchoolTypeModel {
    @SerializedName("schoolingId")
    private int schoolTypeId;
    @SerializedName("schoolName")
    private String schoolTypeName;
    @SerializedName("schoolCategory")
    private ArrayList<SchoolModel> schoolList;
    @SerializedName("other")
    private ArrayList<SchoolModel> otherList;

    public ArrayList<SchoolModel> getOtherList() {
        return otherList;
    }

    public void setOtherList(ArrayList<SchoolModel> otherList) {
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

    public ArrayList<SchoolModel> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(ArrayList<SchoolModel> schoolList) {
        this.schoolList = schoolList;
    }
}
