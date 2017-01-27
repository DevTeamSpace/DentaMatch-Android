package com.appster.dentamatch.interfaces;

import com.appster.dentamatch.network.response.profile.JobTitleList;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 */

public interface JobTitleSelected {
    public void onJobTitleSelected(ArrayList<JobTitleList> jobTitleList);
}
