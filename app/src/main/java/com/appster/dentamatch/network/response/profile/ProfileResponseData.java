package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.model.Skill;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.request.certificates.CertificateRequest;
import com.appster.dentamatch.network.response.affiliation.AffiliationData;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.response.workexp.WorkExpResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 19/01/17.
 */
public class ProfileResponseData {

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
    private ArrayList<AffiliationData> affiliationList;
    @SerializedName("certifications")
    private ArrayList<CertificatesList> certificatesLists;

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

    public ArrayList<AffiliationData> getAffiliationList() {
        return affiliationList;
    }

    public ArrayList<CertificatesList> getCertificatesLists() {
        return certificatesLists;
    }
}
