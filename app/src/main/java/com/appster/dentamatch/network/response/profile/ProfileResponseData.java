package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.model.Skill;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 19/01/17.
 */
public class ProfileResponseData {

    @SerializedName("user")
    private User user;

    @SerializedName("dentalStateBoard")
    private DentalStateBoard dentalStateBoard;

    @SerializedName("licence")
    private LicenceRequest licence;
    @SerializedName("school")
    private ArrayList<School> schoolArrayList;
    @SerializedName("skills")
    private ArrayList<Skill> skillArrayList;
    @SerializedName("affiliations")
    private ArrayList<Skill> affiliationList;
}
