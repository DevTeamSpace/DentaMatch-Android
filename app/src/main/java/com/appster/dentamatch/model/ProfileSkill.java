package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileSkill {
    @SerializedName("id")
    private int parentId;
    @SerializedName("skillName")
    private String skillsName;
    @SerializedName("children")
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
