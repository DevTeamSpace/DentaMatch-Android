package com.appster.dentamatch.network.response.workexp;

import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 13/01/17.
 */
public class WorkExpResponseData {
    public ArrayList<WorkExpRequest> getSaveList() {
        return saveList;
    }

    @SerializedName("list")
    private ArrayList<WorkExpRequest> saveList;
}