package com.appster.dentamatch.presentation.calendar

import android.view.View
import com.appster.dentamatch.R
import kotlinx.android.synthetic.main.single_cell_layout.view.*

class CalendarAvailabilityGridAdapter(
        month: CalendarMonthModel,
        offset: Int,
        listener: CalendarPagerAdapter.CalendarPagerListener?
) : CalendarEventGridAdapter(month, offset, listener) {

    override fun selectDay(day: CalendarDayModel) {
        day.isSelected = !day.isSelected
    }

    override fun isSelected(day: CalendarDayModel) =
            day.isSelected

    override fun makeUnSelected(view: View) {
        view.calendarDateId.background = null
    }

    override fun makeSelected(view: View) {
        view.calendarDateId.setBackgroundResource(R.drawable.shape_temporary_date_selection)
    }
}