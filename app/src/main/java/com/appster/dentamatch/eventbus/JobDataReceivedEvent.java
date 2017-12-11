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
    private int mIsJobSeekerVerified;
    private int mProfileCompleted;

    public int getIsJobSeekerVerified() {
        return mIsJobSeekerVerified;
    }

    public int getProfileCompleted() {
        return mProfileCompleted;
    }






    public JobDataReceivedEvent(ArrayList<SearchJobModel> list, boolean paginationNeeded, int totalItem, int isJobSeekerVerified,int profileCompleted){
        mJobList = list;
        mPaginationNeeded = paginationNeeded;
        mTotalItem = totalItem;

        mIsJobSeekerVerified=isJobSeekerVerified;
        mProfileCompleted=profileCompleted;

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
