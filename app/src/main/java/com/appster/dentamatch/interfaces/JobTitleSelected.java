package com.appster.dentamatch.interfaces;


import com.appster.dentamatch.model.JobTitleListModel;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 * To inject activity reference.
 */

public interface JobTitleSelected {
     void onJobTitleSelected(ArrayList<JobTitleListModel> jobTitleList);
}
