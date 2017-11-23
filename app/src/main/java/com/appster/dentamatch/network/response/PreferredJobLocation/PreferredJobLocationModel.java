package com.appster.dentamatch.network.response.PreferredJobLocation;

import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zishan on 22/11/17.
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
        private List<PreferredJobLocationData> preferredJobLocations = null;

        public List<PreferredJobLocationData> getPreferredJobLocations() {
            return preferredJobLocations;
        }

        public void setPreferredJobLocations(List<PreferredJobLocationData> preferredJobLocations) {
            this.preferredJobLocations = preferredJobLocations;
        }
    }
}
