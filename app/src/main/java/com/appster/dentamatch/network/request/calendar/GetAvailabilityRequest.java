/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.calendar;

/**
 * Created by virender on 07/02/17.
 * To inject activity reference.
 */
public class GetAvailabilityRequest {
    private String calendarStartDate;
    private String calendarEndDate;

    public void setCalendarStartDate(String calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public void setCalendarEndDate(String calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }
}
