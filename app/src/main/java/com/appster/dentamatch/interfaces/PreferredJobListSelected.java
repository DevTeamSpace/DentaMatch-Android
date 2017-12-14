package com.appster.dentamatch.interfaces;


import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 */

public interface PreferredJobListSelected {
     void onPrefJobSelected(ArrayList<PreferredJobLocationData> jobTitleList);
}
