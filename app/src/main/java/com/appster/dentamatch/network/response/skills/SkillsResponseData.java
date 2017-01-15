package com.appster.dentamatch.network.response.skills;

import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.Skill;
import com.appster.dentamatch.network.response.auth.UserDetail;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold Skills response data.
 */
public class SkillsResponseData {

    @SerializedName("list")
    private ArrayList<ParentSkill> skillsList;

    public List<ParentSkill> getSkillsList() {
        return skillsList;
    }
}
