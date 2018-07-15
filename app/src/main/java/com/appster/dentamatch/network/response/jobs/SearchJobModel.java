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

/**
 * Created by Appster on 27/01/17.
 * To inject activity reference.
 */

public class SearchJobModel implements Parcelable{
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
    private int matchedSkills;
    private int templateSkillsCount;
    private double percentaSkillsMatch;

    public int getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(int matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public int getTemplateSkillsCount() {
        return templateSkillsCount;
    }

    public void setTemplateSkillsCount(int templateSkillsCount) {
        this.templateSkillsCount = templateSkillsCount;
    }

    public double getPercentaSkillsMatch() {
        return percentaSkillsMatch;
    }

    public void setPercentaSkillsMatch(double percentaSkillsMatch) {
        this.percentaSkillsMatch = percentaSkillsMatch;
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

    public void setIsSaved(int isSaved) {
        this.isSaved = isSaved;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.jobType);
        dest.writeInt(this.isMonday);
        dest.writeInt(this.isTuesday);
        dest.writeInt(this.isWednesday);
        dest.writeInt(this.isThursday);
        dest.writeInt(this.isFriday);
        dest.writeInt(this.isSaturday);
        dest.writeInt(this.isSunday);
        dest.writeString(this.jobTitleName);
        dest.writeString(this.officeName);
        dest.writeString(this.address);
        dest.writeInt(this.zipCode);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.createdAt);
        dest.writeInt(this.days);
        dest.writeInt(this.isSaved);
        dest.writeDouble(this.distance);
        dest.writeInt(this.matchedSkills);
        dest.writeInt(this.templateSkillsCount);
        dest.writeDouble(this.percentaSkillsMatch);
    }

    public SearchJobModel() {
    }

    private SearchJobModel(Parcel in) {
        this.id = in.readInt();
        this.jobType = in.readInt();
        this.isMonday = in.readInt();
        this.isTuesday = in.readInt();
        this.isWednesday = in.readInt();
        this.isThursday = in.readInt();
        this.isFriday = in.readInt();
        this.isSaturday = in.readInt();
        this.isSunday = in.readInt();
        this.jobTitleName = in.readString();
        this.officeName = in.readString();
        this.address = in.readString();
        this.zipCode = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.createdAt = in.readString();
        this.days = in.readInt();
        this.isSaved = in.readInt();
        this.distance = in.readDouble();
        this.matchedSkills = in.readInt();
        this.templateSkillsCount = in.readInt();
        this.percentaSkillsMatch = in.readDouble();
    }

    public static final Creator<SearchJobModel> CREATOR = new Creator<SearchJobModel>() {
        @Override
        public SearchJobModel createFromParcel(Parcel source) {
            return new SearchJobModel(source);
        }

        @Override
        public SearchJobModel[] newArray(int size) {
            return new SearchJobModel[size];
        }
    };
}
