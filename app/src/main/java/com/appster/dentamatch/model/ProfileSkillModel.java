/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class ProfileSkillModel {
    @SerializedName("id")
    private int parentId;
    @SerializedName("skillName")
    private String skillsName;
    @SerializedName("children")
    private ArrayList<ProfileSubSkillModel> childSkillList;

    public int getParentId() {
        return parentId;
    }

    public String getSkillsName() {
        return skillsName;
    }

    public ArrayList<ProfileSubSkillModel> getChildSkillList() {
        return childSkillList;
    }
}
