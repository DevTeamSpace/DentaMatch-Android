/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.network.response.jobs.SearchJobModel;

import java.util.ArrayList;

/**
 * Created by Appster on 22/02/17.
 * To inject activity reference.
 */

public class TrackJobListRetrievedEvent {
    private final ArrayList<SearchJobModel> mData;
    private final int type;

    public TrackJobListRetrievedEvent(ArrayList<SearchJobModel> mData, int type){
        this.mData = mData;
        this.type = type;
    }

    public ArrayList<SearchJobModel> getData() {
        return mData;
    }

    public int getType() {
        return type;
    }
}
