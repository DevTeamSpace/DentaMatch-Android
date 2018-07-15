/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.interfaces;


import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 * To inject activity reference.
 */

public interface PreferredJobListSelected {
     void onPrefJobSelected(ArrayList<PreferredJobLocationData> jobTitleList);
}
