package com.appster.dentamatch.network.response.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 08/02/17.
 * To inject activity reference.
 */
public class AvailabilityResponseData {
    @SerializedName("calendarAvailability")
    private CalendarAvailability calendarAvailability;

    @SerializedName("tempDatesAvailability")
    private ArrayList<String> tempDateList;

    public CalendarAvailability getCalendarAvailability() {
        return calendarAvailability;
    }

    public ArrayList<String> getTempDateList() {
        return tempDateList;
    }
}
