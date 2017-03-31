package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileSubSkillModel {
    @SerializedName("id")
    private int childId;
    @SerializedName("skillName")
    private String skillsChildName;
    private String otherSkills;

    public String getOtherSkills() {
        return otherSkills;
    }

    public int getChildId() {
        return childId;
    }

    public String getSkillsChildName() {
        return skillsChildName;
    }

}
