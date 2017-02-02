package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 01/02/17.
 */

public class JobDetailModel {

    private int id;
    private int jobType;
    private int isMonday;
    private int isTuesday;
    private int isWednesday;
    private int isThursday;
    private int isFriday;
    private int isSaturday;
    private int isSunday;
    private String templateName;
    private String templateDesc;
    private String workEverydayStart;
    private String workEverydayEnd;
    private String mondayStart;
    private String mondayEnd;
    private String tuesdayStart;
    private String tuesdayEnd;
    private String wednesdayStart;
    private String wednesdayEnd;
    private String thursdayStart;
    private String thursdayEnd;
    private String fridayStart;
    private String saturdayStart;
    private String saturdayEnd;
    private String sundayStart;
    private String sundayEnd;
    @SerializedName("jobtitleName")
    private String jobTitleName;
    private String officeName;
    private String address;
    @SerializedName("zipcode")
    private int zipCode;
    private double latitude;
    private double longitude;
    private String createdAt;
    private int jobPostedTimeGap;
    private String officeTypeName;
    private double distance;
//    public List<Object> jobTypeDates = null;
    private String jobTypeString;
    private int isApplied;
    private int isSaved;
    private String partTimeDays;

    public int getId() {
        return id;
    }

    public int getJobType() {
        return jobType;
    }

    public int getIsMonday() {
        return isMonday;
    }

    public int getIsTuesday() {
        return isTuesday;
    }

    public int getIsWednesday() {
        return isWednesday;
    }

    public int getIsThursday() {
        return isThursday;
    }

    public int getIsFriday() {
        return isFriday;
    }

    public int getIsSaturday() {
        return isSaturday;
    }

    public int getIsSunday() {
        return isSunday;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public String getWorkEverydayStart() {
        return workEverydayStart;
    }

    public String getWorkEverydayEnd() {
        return workEverydayEnd;
    }

    public String getMondayStart() {
        return mondayStart;
    }

    public String getMondayEnd() {
        return mondayEnd;
    }

    public String getTuesdayStart() {
        return tuesdayStart;
    }

    public String getTuesdayEnd() {
        return tuesdayEnd;
    }

    public String getWednesdayStart() {
        return wednesdayStart;
    }

    public String getWednesdayEnd() {
        return wednesdayEnd;
    }

    public String getThursdayStart() {
        return thursdayStart;
    }

    public String getThursdayEnd() {
        return thursdayEnd;
    }

    public String getFridayStart() {
        return fridayStart;
    }

    public String getSaturdayStart() {
        return saturdayStart;
    }

    public String getSaturdayEnd() {
        return saturdayEnd;
    }

    public String getSundayStart() {
        return sundayStart;
    }

    public String getSundayEnd() {
        return sundayEnd;
    }

    public String getJobTitleName() {
        return jobTitleName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getAddress() {
        return address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getJobPostedTimeGap() {
        return jobPostedTimeGap;
    }

    public String getOfficeTypeName() {
        return officeTypeName;
    }

    public double getDistance() {
        return distance;
    }

    public String getJobTypeString() {
        return jobTypeString;
    }

    public int getIsApplied() {
        return isApplied;
    }

    public int getIsSaved() {
        return isSaved;
    }
}