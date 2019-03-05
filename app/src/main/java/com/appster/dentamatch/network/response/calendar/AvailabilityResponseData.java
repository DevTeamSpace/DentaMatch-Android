/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by virender on 08/02/17.
 * To inject activity reference.
 */
public class AvailabilityResponseData {
    @SerializedName("calendarAvailability")
    private CalendarAvailability calendarAvailability;

    @SerializedName("tempDatesAvailability")
    private ArrayList<Date> tempDateList;

    public CalendarAvailability getCalendarAvailability() {
        return calendarAvailability;
    }

    public ArrayList<Date> getTempDateList() {
        return tempDateList;
    }
}
