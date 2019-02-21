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

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class ProfileSchoolModel implements Parcelable {
    private int id;
    private int childId;
    private String schoolName;
    private String schoolChildName;
    private String schoolTitle;
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

    public String getSchoolTitle() {
        return schoolTitle;
    }

    public String getOtherSchooling() {
        return otherSchooling;
    }

    public int getYearOfGraduation() {
        return yearOfGraduation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.childId);
        dest.writeString(this.schoolName);
        dest.writeString(this.schoolChildName);
        dest.writeString(this.schoolTitle);
        dest.writeString(this.otherSchooling);
        dest.writeInt(this.yearOfGraduation);
    }

    public ProfileSchoolModel() {
    }

    protected ProfileSchoolModel(Parcel in) {
        this.id = in.readInt();
        this.childId = in.readInt();
        this.schoolName = in.readString();
        this.schoolChildName = in.readString();
        this.schoolTitle = in.readString();
        this.otherSchooling = in.readString();
        this.yearOfGraduation = in.readInt();
    }

    public static final Parcelable.Creator<ProfileSchoolModel> CREATOR = new Parcelable.Creator<ProfileSchoolModel>() {
        @Override
        public ProfileSchoolModel createFromParcel(Parcel source) {
            return new ProfileSchoolModel(source);
        }

        @Override
        public ProfileSchoolModel[] newArray(int size) {
            return new ProfileSchoolModel[size];
        }
    };
}
