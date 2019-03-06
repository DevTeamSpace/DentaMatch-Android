package com.appster.dentamatch.presentation.calendar

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_calendar_pager.*

class CalendarAvailabilityPagerFragment : CalendarPagerFragment() {

    companion object {

        fun newInstance(month: CalendarMonthModel) =
                CalendarAvailabilityPagerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(CalendarPagerFragment.ARGS_MONTH, month)
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (month != null) {
            calendarGrid.adapter = CalendarAvailabilityGridAdapter(month!!, offset, listener)
        }
    }
}