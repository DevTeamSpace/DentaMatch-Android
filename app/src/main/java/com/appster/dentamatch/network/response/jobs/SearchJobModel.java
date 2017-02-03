package com.appster.dentamatch.network.response.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 27/01/17.
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

    protected SearchJobModel(Parcel in) {
        id = in.readInt();
        jobType = in.readInt();
        isMonday = in.readInt();
        isTuesday = in.readInt();
        isWednesday = in.readInt();
        isThursday = in.readInt();
        isFriday = in.readInt();
        isSaturday = in.readInt();
        isSunday = in.readInt();
        jobTitleName = in.readString();
        officeName = in.readString();
        address = in.readString();
        zipCode = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        createdAt = in.readString();
        days = in.readInt();
        isSaved = in.readInt();
        distance = in.readDouble();
    }

    public static final Creator<SearchJobModel> CREATOR = new Creator<SearchJobModel>() {
        @Override
        public SearchJobModel createFromParcel(Parcel in) {
            return new SearchJobModel(in);
        }

        @Override
        public SearchJobModel[] newArray(int size) {
            return new SearchJobModel[size];
        }
    };

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
        dest.writeInt(id);
        dest.writeInt(jobType);
        dest.writeInt(isMonday);
        dest.writeInt(isTuesday);
        dest.writeInt(isWednesday);
        dest.writeInt(isThursday);
        dest.writeInt(isFriday);
        dest.writeInt(isSaturday);
        dest.writeInt(isSunday);
        dest.writeString(jobTitleName);
        dest.writeString(officeName);
        dest.writeString(address);
        dest.writeInt(zipCode);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(createdAt);
        dest.writeInt(days);
        dest.writeInt(isSaved);
        dest.writeDouble(distance);
    }
}
