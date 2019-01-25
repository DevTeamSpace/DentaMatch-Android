/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.skills;

import com.appster.dentamatch.base.BaseResponse;
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