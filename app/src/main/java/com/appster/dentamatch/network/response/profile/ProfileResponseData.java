/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.appster.dentamatch.eventbus.LocationEvent;
import com.appster.dentamatch.model.DentalStateBoardModel;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.model.ProfileSchoolModel;
import com.appster.dentamatch.model.ProfileSkillModel;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.request.auth.LicenseRequest;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.workexp.WorkExpResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 19/01/17.
 * To inject activity reference.
 */

public class ProfileResponseData implements Parcelable {

    @SerializedName("user")
    private UserModel userModel;
    @SerializedName("workExperience")
    private WorkExpResponseData workExperience;

    @SerializedName("dentalStateBoard")
    private DentalStateBoardModel dentalStateBoard;

    @SerializedName("licence")
    private LicenseRequest licence;
    @SerializedName("school")
    private ArrayList<ProfileSchoolModel> schoolArrayList;
    @SerializedName("skills")
    private ArrayList<ProfileSkillModel> skillArrayList;
    @SerializedName("affiliations")
    private ArrayList<LocationEvent.Affiliation> affiliationList;
    @SerializedName("certifications")
    private ArrayList<CertificatesList> certificatesLists;
    @SerializedName("joblists")
    private ArrayList<JobTitleListModel> jobTitleLists;


    public UserModel getUser() {
        return userModel;
    }

    public WorkExpResponseData getWorkExperience() {
        return workExperience;
    }

    public DentalStateBoardModel getDentalStateBoard() {
        return dentalStateBoard;
    }

    public LicenseRequest getLicence() {
        return licence;
    }

    public ArrayList<ProfileSchoolModel> getSchoolArrayList() {
        return schoolArrayList;
    }

    public ArrayList<ProfileSkillModel> getSkillArrayList() {
        return skillArrayList;
    }

    public ArrayList<LocationEvent.Affiliation> getAffiliationList() {
        return affiliationList;
    }

    public ArrayList<CertificatesList> getCertificatesLists() {
        return certificatesLists;
    }

    public ArrayList<JobTitleListModel> getJobTitleLists() {
        return jobTitleLists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userModel, flags);
        dest.writeParcelable(this.workExperience, flags);
        dest.writeParcelable(this.dentalStateBoard, flags);
        dest.writeParcelable(this.licence, flags);
        dest.writeList(this.schoolArrayList);
        dest.writeList(this.skillArrayList);
        dest.writeList(this.affiliationList);
        dest.writeTypedList(this.certificatesLists);
        dest.writeTypedList(this.jobTitleLists);
    }

    public ProfileResponseData() {
    }

    protected ProfileResponseData(Parcel in) {
        this.userModel = in.readParcelable(UserModel.class.getClassLoader());
        this.workExperience = in.readParcelable(WorkExpResponseData.class.getClassLoader());
        this.dentalStateBoard = in.readParcelable(DentalStateBoardModel.class.getClassLoader());
        this.licence = in.readParcelable(LicenseRequest.class.getClassLoader());
        this.schoolArrayList = new ArrayList<>();
        in.readList(this.schoolArrayList, ProfileSchoolModel.class.getClassLoader());
        this.skillArrayList = new ArrayList<>();
        in.readList(this.skillArrayList, ProfileSkillModel.class.getClassLoader());
        this.affiliationList = new ArrayList<>();
        in.readList(this.affiliationList, LocationEvent.Affiliation.class.getClassLoader());
        this.certificatesLists = in.createTypedArrayList(CertificatesList.CREATOR);
        this.jobTitleLists = in.createTypedArrayList(JobTitleListModel.CREATOR);
    }

    public static final Parcelable.Creator<ProfileResponseData> CREATOR = new Parcelable.Creator<ProfileResponseData>() {
        @Override
        public ProfileResponseData createFromParcel(Parcel source) {
            return new ProfileResponseData(source);
        }

        @Override
        public ProfileResponseData[] newArray(int size) {
            return new ProfileResponseData[size];
        }
    };
}
