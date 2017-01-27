package com.appster.dentamatch.network.response.jobs;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 27/01/17.
 */

public class SearchJobModel {
    private int id;
    private int jobType;
    private int isMonday;
    private int isTuesday;
    private int isWednesday;
    private int isThursday;
    private int isFriday;
    private int isSaturday;
    private int isSunday;
    @SerializedName("jobtitleName")
    private String jobTitleName;
    private String officeName;
    private String address;
    @SerializedName("zipcode")
    private int zipCode;
    private double latitude;
    private double longitude;
    private String createdAt;
    private int days;
    private int isSaved;
    private double distance;

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

    public int getDays() {
        return days;
    }

    public int getIsSaved() {
        return isSaved;
    }

    public double getDistance() {
        return distance;
    }
}
