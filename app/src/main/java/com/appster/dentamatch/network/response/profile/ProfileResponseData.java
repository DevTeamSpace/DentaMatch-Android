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
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.workexp.WorkExpResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 19/01/17.
 * To inject activity reference.
 */
public class ProfileResponseData {

    @SerializedName("user")
    private UserModel userModel;
    @SerializedName("workExperience")
    private WorkExpResponseData workExperience;

    @SerializedName("dentalStateBoard")
    private DentalStateBoardModel dentalStateBoard;

    @SerializedName("licence")
    private LicenceRequest licence;
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

    public LicenceRequest getLicence() {
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

}
