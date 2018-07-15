package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ramkumar on 12/01/17.
 * To inject activity reference.
 */

public class ParentSkillModel {
    private int id;
    private int parentId;
    private String skillName;
    @SerializedName("children")
    private ArrayList<SubSkillModel> subSkills;
    private String otherSkill;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public ArrayList<SubSkillModel> getSubSkills() {
        return subSkills;
    }

    public void setSubSkills(ArrayList<SubSkillModel> subSkills) {
        this.subSkills = subSkills;
    }

    public String getOtherSkill() {
        return otherSkill;
    }

    public void setOtherSkill(String otherSkill) {
        this.otherSkill = otherSkill;
    }

}
