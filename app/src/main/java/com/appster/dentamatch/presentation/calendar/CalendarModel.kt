package com.appster.dentamatch.presentation.calendar

import java.util.*

data class CalendarModel(
        var isFullTime: Boolean = false,
        val months: ArrayList<CalendarMonthModel> = ArrayList()
) {
    companion object {
        fun getInstance() = CalendarModel().apply {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            for (i in 0..calendar.getActualMaximum(Calendar.MONTH)) {
                months.add(CalendarMonthModel().apply {
                    calendar.set(Calendar.MONTH, i)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    this.offset = calendar.get(Calendar.DAY_OF_WEEK) - 1
                    for (j in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        this.days.add(CalendarDayModel(text = (j).toString()))
                    }
                    if (currentMonth == i) {
                        this.selected = this.days[currentDay - 1]
                    } else {
                        this.selected = this.days[0]
                    }
                    this.selected
                })
            }
        }
    }
}