package com.appster.dentamatch.network.response.skills;

import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.response.auth.LoginResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * Response class for Skills and Experience.
 */
public class SkillsResponse extends BaseResponse {

    @SerializedName("result")
    private SkillsResponseData skillsResponseData;

    public SkillsResponseData getSkillsResponseData() {
        return skillsResponseData;
    }

    public void setSkillsResponseData(SkillsResponseData skillsResponseData) {
        this.skillsResponseData = skillsResponseData;
    }
}