/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.skills;

import com.appster.dentamatch.network.request.certificates.UpdateCertificates;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ram on 18/01/17.
 * To inject activity reference.
 */
public class SkillsUpdateRequest {

    private ArrayList<Integer> skills;

    @SerializedName("other")
    private ArrayList<UpdateCertificates> others;

    public void setSkills(ArrayList<Integer> skills) {
        this.skills = skills;
    }

    public void setOther(ArrayList<UpdateCertificates> others) {
        this.others = others;
    }
}
