package com.appster.dentamatch.network.response.profile;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileSchool {
    private int id;
    private int childId;
    private String schoolName;
    private String schoolChildName;
    private String otherSchooling;
    private int yearOfGraduation;

    public int getId() {
        return id;
    }

    public int getChildId() {
        return childId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolChildName() {
        return schoolChildName;
    }

    public String getOtherSchooling() {
        return otherSchooling;
    }

    public int getYearOfGraduation() {
        return yearOfGraduation;
    }
}