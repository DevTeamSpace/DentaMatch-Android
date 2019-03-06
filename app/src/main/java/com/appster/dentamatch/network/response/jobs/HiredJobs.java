/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 10/02/17.
 * To inject activity reference.
 */
public class HiredJobs implements Parcelable {
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
    private int payRate;

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

    public int getPayRate() {
        return payRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recruiterJobId);
        dest.writeInt(this.id);
        dest.writeInt(this.jobType);
        dest.writeInt(this.isMonday);
        dest.writeInt(this.isTuesday);
        dest.writeInt(this.isWednesday);
        dest.writeInt(this.isThursday);
        dest.writeInt(this.isFriday);
        dest.writeInt(this.isSaturday);
        dest.writeInt(this.isSunday);
        dest.writeString(this.jobtitleName);
        dest.writeString(this.officeName);
        dest.writeString(this.address);
        dest.writeString(this.jobCreatedAt);
        dest.writeString(this.jobAppliedOn);
        dest.writeString(this.jobDate);
        dest.writeString(this.jobTypeString);
        dest.writeInt(this.days);
        dest.writeInt(this.zipcode);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.tempDates);
        dest.writeList(this.temporaryJobDates);
        dest.writeString(this.currentDate);
        dest.writeInt(this.payRate);
    }

    public HiredJobs() {
    }

    protected HiredJobs(Parcel in) {
        this.recruiterJobId = in.readInt();
        this.id = in.readInt();
        this.jobType = in.readInt();
        this.isMonday = in.readInt();
        this.isTuesday = in.readInt();
        this.isWednesday = in.readInt();
        this.isThursday = in.readInt();
        this.isFriday = in.readInt();
        this.isSaturday = in.readInt();
        this.isSunday = in.readInt();
        this.jobtitleName = in.readString();
        this.officeName = in.readString();
        this.address = in.readString();
        this.jobCreatedAt = in.readString();
        this.jobAppliedOn = in.readString();
        this.jobDate = in.readString();
        this.jobTypeString = in.readString();
        this.days = in.readInt();
        this.zipcode = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.tempDates = in.readString();
        this.temporaryJobDates = new ArrayList<TemporaryJobDates>();
        in.readList(this.temporaryJobDates, TemporaryJobDates.class.getClassLoader());
        this.currentDate = in.readString();
        this.payRate = in.readInt();
    }

    public static final Parcelable.Creator<HiredJobs> CREATOR = new Parcelable.Creator<HiredJobs>() {
        @Override
        public HiredJobs createFromParcel(Parcel source) {
            return new HiredJobs(source);
        }

        @Override
        public HiredJobs[] newArray(int size) {
            return new HiredJobs[size];
        }
    };
}
