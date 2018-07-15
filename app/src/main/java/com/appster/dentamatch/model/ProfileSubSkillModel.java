/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class ProfileSubSkillModel {
    @SerializedName("id")
    private int childId;
    @SerializedName("skillName")
    private String skillsChildName;
    private String otherSkill;

    public String getOtherSkills() {
        return otherSkill;
    }

    public int getChildId() {
        return childId;
    }

    public String getSkillsChildName() {
        return skillsChildName;
    }

}
