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

/**
 * Created by virender on 12/01/17.
 * To inject activity reference.
 */
public class LicenseRequest implements Parcelable {

    private String license;
    private String licenseNumber;
    private String state;
    private int jobTitleId;
    private String aboutMe;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public LicenseRequest() { }

    private LicenseRequest(Parcel in) {
        license = in.readString();
        licenseNumber = in.readString();
        state = in.readString();
        jobTitleId = in.readInt();
        aboutMe = in.readString();
    }

    public static final Creator<LicenseRequest> CREATOR = new Creator<LicenseRequest>() {
        @Override
        public LicenseRequest createFromParcel(Parcel in) {
            return new LicenseRequest(in);
        }

        @Override
        public LicenseRequest[] newArray(int size) {
            return new LicenseRequest[size];
        }
    };

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

    public int getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(license);
        parcel.writeString(licenseNumber);
        parcel.writeString(state);
        parcel.writeInt(jobTitleId);
        parcel.writeString(aboutMe);
    }
}


