/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.interfaces;

import java.util.Calendar;

/**
 * Created by bawenderyandra on 29/03/17.
 * To inject activity reference.
 */

public interface OnDateSelected {
     void selectedDate(String date);
     void onMonthChanged(Calendar cal);
}
