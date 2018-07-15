package com.appster.dentamatch.network.response.jobs;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 10/02/17.
 * To inject activity reference.
 */
public class HiredJobs {
    private int recruiterJobId;
    private int id;
    private int jobType;
    private int isMonday;
    private int isTuesday;
    private int isWednesday;
    private int isThursday;
    private int isFriday;
    private int isSaturday;
    private int isSunday;
    private String jobtitleName;
    private String officeName;
    private String address;
    private String jobCreatedAt;
    private String jobAppliedOn;
    private String jobDate;
    private String jobTypeString;
    private int days;
    private int zipcode;
    private double latitude;
    private double longitude;
    private String tempDates;
    @SerializedName("tempJobDates")
    private ArrayList<TemporaryJobDates> temporaryJobDates;
    private String currentDate;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getRecruiterJobId() {
        return recruiterJobId;
    }

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

    public String getTempDates() {
        return tempDates;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getAddress() {
        return address;
    }

    public String getJobCreatedAt() {
        return jobCreatedAt;
    }

    public String getJobAppliedOn() {
        return jobAppliedOn;
    }

    public String getJobDate() {
        return jobDate;
    }

    public String getJobTypeString() {
        return jobTypeString;
    }

    public int getDays() {
        return days;
    }

    public int getZipcode() {
        return zipcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<TemporaryJobDates> getTemporaryJobDates() {
        return temporaryJobDates;
    }
}
