/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.auth;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by virender on 12/01/17.
 * To inject activity reference.
 */
public class LicenseRequest implements Parcelable {

    @Nullable
    private String license;
    @Nullable
    private String licenseNumber;
    @Nullable
    private String state;
    @Nullable
    private Integer jobTitleId;
    @Nullable
    private String aboutMe;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Integer jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.license);
        dest.writeString(this.licenseNumber);
        dest.writeString(this.state);
        dest.writeValue(this.jobTitleId);
        dest.writeString(this.aboutMe);
    }

    public LicenseRequest() {
    }

    protected LicenseRequest(Parcel in) {
        this.license = in.readString();
        this.licenseNumber = in.readString();
        this.state = in.readString();
        this.jobTitleId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.aboutMe = in.readString();
    }

    public static final Parcelable.Creator<LicenseRequest> CREATOR = new Parcelable.Creator<LicenseRequest>() {
        @Override
        public LicenseRequest createFromParcel(Parcel source) {
            return new LicenseRequest(source);
        }

        @Override
        public LicenseRequest[] newArray(int size) {
            return new LicenseRequest[size];
        }
    };
}


