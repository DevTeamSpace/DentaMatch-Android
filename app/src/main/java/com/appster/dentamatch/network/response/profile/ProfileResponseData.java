package com.appster.dentamatch.network.response.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.appster.dentamatch.model.DentalStateBoard;
import com.appster.dentamatch.model.LocationEvent;
import com.appster.dentamatch.model.ProfileSchool;
import com.appster.dentamatch.model.ProfileSkill;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.network.request.affiliation.Affiliation;
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
    private User user;
    @SerializedName("workExperience")
    private WorkExpResponseData workExperience;

    @SerializedName("dentalStateBoard")
    private DentalStateBoard dentalStateBoard;

    @SerializedName("licence")
    private LicenceRequest licence;
    @SerializedName("school")
    private ArrayList<ProfileSchool> schoolArrayList;
    @SerializedName("skills")
    private ArrayList<ProfileSkill> skillArrayList;
    @SerializedName("affiliations")
    private ArrayList<Affiliation> affiliationList;
    @SerializedName("certifications")
    private ArrayList<CertificatesList> certificatesLists;

    private ProfileResponseData(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
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
        dest.writeParcelable(user, flags);
    }

    public User getUser() {
        return user;
    }

    public WorkExpResponseData getWorkExperience() {
        return workExperience;
    }

    public DentalStateBoard getDentalStateBoard() {
        return dentalStateBoard;
    }

    public LicenceRequest getLicence() {
        return licence;
    }

    public ArrayList<ProfileSchool> getSchoolArrayList() {
        return schoolArrayList;
    }

    public ArrayList<ProfileSkill> getSkillArrayList() {
        return skillArrayList;
    }

    public ArrayList<Affiliation> getAffiliationList() {
        return affiliationList;
    }

    public ArrayList<CertificatesList> getCertificatesLists() {
        return certificatesLists;
    }
}
