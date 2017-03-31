package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.network.response.jobs.SearchJobModel;

import java.util.ArrayList;

/**
 * Created by Appster on 28/01/17.
 */

public class JobDataReceivedEvent {
    private ArrayList<SearchJobModel> mJobList;
    private boolean mPaginationNeeded;
    private int mTotalItem;



    public JobDataReceivedEvent(ArrayList<SearchJobModel> list, boolean paginationNeeded, int totalItem){
        mJobList = list;
        mPaginationNeeded = paginationNeeded;
        mTotalItem = totalItem;

    }

    public ArrayList<SearchJobModel> getJobList() {
        return mJobList;
    }

    public boolean isPaginationNeeded() {
        return mPaginationNeeded;
    }

    public int getTotalItem() {
        return mTotalItem;
    }
}
