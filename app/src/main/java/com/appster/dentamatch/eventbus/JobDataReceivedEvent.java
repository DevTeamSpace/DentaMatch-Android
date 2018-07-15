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
 * Created by Appster on 28/01/17.
 * To stream job data received events.
 */

public class JobDataReceivedEvent {
    private final ArrayList<SearchJobModel> mJobList;
    private final boolean mPaginationNeeded;
    private final int mTotalItem;
    private final int mIsJobSeekerVerified;
    private final int mProfileCompleted;

    public int getIsJobSeekerVerified() {
        return mIsJobSeekerVerified;
    }

    public int getProfileCompleted() {
        return mProfileCompleted;
    }


    public JobDataReceivedEvent(ArrayList<SearchJobModel> list, boolean paginationNeeded, int totalItem, int isJobSeekerVerified, int profileCompleted) {
        mJobList = list;
        mPaginationNeeded = paginationNeeded;
        mTotalItem = totalItem;

        mIsJobSeekerVerified = isJobSeekerVerified;
        mProfileCompleted = profileCompleted;

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
