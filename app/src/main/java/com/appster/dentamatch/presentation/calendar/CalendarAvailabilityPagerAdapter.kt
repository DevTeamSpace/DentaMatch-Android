package com.appster.dentamatch.presentation.calendar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class CalendarAvailabilityPagerAdapter(
        private val listener: CalendarPagerListener,
        fragmentManager: FragmentManager
) : CalendarPagerAdapter(listener, fragmentManager) {

    override fun getItem(p0: Int): Fragment =
        CalendarAvailabilityPagerFragment.newInstance(getMonth(p0)).apply {
            this.setCalendarPagerListener(listener)
        }
}