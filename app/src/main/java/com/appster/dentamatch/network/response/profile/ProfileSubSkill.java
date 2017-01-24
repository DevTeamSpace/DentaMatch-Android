package com.appster.dentamatch.network.response.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileSubSkill {
    @SerializedName(" id")
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
