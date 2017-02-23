package com.appster.dentamatch.model;

import com.appster.dentamatch.network.response.jobs.SearchJobModel;

import java.util.ArrayList;

/**
 * Created by Appster on 22/02/17.
 */

public class TrackJobListRetrievedEvent {
    private ArrayList<SearchJobModel> mData;
    private int type;

    public TrackJobListRetrievedEvent(ArrayList<SearchJobModel> mData, int type){
        this.mData = mData;
        this.type = type;
    }

    public ArrayList<SearchJobModel> getmData() {
        return mData;
    }

    public int getType() {
        return type;
    }
}
