/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 * To inject activity reference.
 */
public class JobTitleListModel implements Parcelable {


    private int id;
    @SerializedName("jobtitleName")
    private String jobTitle;
    private String shortName;
    private boolean isSelected;
    private int isLicenseRequired;

    public int getIsLicenseRequired() {
        return isLicenseRequired;
    }

    public void setIsLicenseRequired(int isLicenseRequired) {
        this.isLicenseRequired = isLicenseRequired;
    }

    public JobTitleListModel() {

    }

    private JobTitleListModel(Parcel in) {
        id = in.readInt();
        jobTitle = in.readString();
        shortName = in.readString();
        isLicenseRequired = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<JobTitleListModel> CREATOR = new Creator<JobTitleListModel>() {
        @Override
        public JobTitleListModel createFromParcel(Parcel in) {
            return new JobTitleListModel(in);
        }

        @Override
        public JobTitleListModel[] newArray(int size) {
            return new JobTitleListModel[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(jobTitle);
        dest.writeString(shortName);
        dest.writeInt(isLicenseRequired);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
