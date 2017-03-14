package com.appster.dentamatch.network.response.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.appster.dentamatch.model.DentalStateBoardModel;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.EventBus.LocationEvent;
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
 */
public class ProfileResponseData implements Parcelable {

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


    private ProfileResponseData(Parcel in) {
        userModel = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<ProfileResponseData> CREATOR = new Creator<ProfileResponseData>() {
        @Override
        public ProfileResponseData createFromParcel(Parcel in) {
            return new ProfileResponseData(in);
        }

        @Override
        public ProfileResponseData[] newArray(int size) {
            return new ProfileResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userModel, flags);
    }

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
