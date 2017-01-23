package com.appster.dentamatch.network.response.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileSkill {
    private int parentId;
    private String skillsName;
    @SerializedName("childSkills")
    private ArrayList<ProfileSubSkill> childSkillList;

    public int getParentId() {
        return parentId;
    }

    public String getSkillsName() {
        return skillsName;
    }

    public ArrayList<ProfileSubSkill> getChildSkillList() {
        return childSkillList;
    }
}
