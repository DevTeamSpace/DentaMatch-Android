package com.appster.dentamatch.presentation.calendar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kotlin.collections.ArrayList

open class CalendarPagerAdapter(
        private val listener: CalendarPagerListener,
        fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    private var data = ArrayList<CalendarMonthModel>()

    interface CalendarPagerListener {
        fun onMonthChanged(month: Int)
        fun onDaySelected(day: CalendarDayModel)
    }

    fun setData(months: ArrayList<CalendarMonthModel>) {
        data = months
        notifyDataSetChanged()
    }

    protected fun getMonth(position: Int) = data[position]

    override fun getItem(p0: Int): Fragment =
            CalendarPagerFragment.newInstance(getMonth(p0)).apply {
                this.setCalendarPagerListener(listener)
            }

    override fun getCount() = data.size
}