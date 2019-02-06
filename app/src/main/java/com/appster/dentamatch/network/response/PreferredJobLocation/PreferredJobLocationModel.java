/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.PreferredJobLocation;

import com.appster.dentamatch.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zishan on 22/11/17.
 * To inject activity reference.
 */

public class PreferredJobLocationModel extends BaseResponse {
    private PreferredJobResult result;

    public PreferredJobResult getResult() {
        return result;
    }

    public void setResult(PreferredJobResult result) {
        this.result = result;
    }

    public class PreferredJobResult{
        private ArrayList<PreferredJobLocationData> preferredJobLocations = null;

        public ArrayList<PreferredJobLocationData> getPreferredJobLocations() {
            return preferredJobLocations;
        }

        public void setPreferredJobLocations(ArrayList<PreferredJobLocationData> preferredJobLocations) {
            this.preferredJobLocations = preferredJobLocations;
        }
    }
}
