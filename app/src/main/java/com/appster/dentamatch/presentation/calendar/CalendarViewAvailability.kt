package com.appster.dentamatch.presentation.calendar

import android.content.Context
import android.support.v4.app.FragmentManager
import android.util.AttributeSet
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewAvailability(
        context: Context,
        attributeSet: AttributeSet?
) : CalendarView(context, attributeSet) {

    override fun initializePagerAdapter(fragmentManager: FragmentManager) {
        mPagerAdapter = CalendarAvailabilityPagerAdapter(this, fragmentManager)
        mCalendarModel = CalendarModel.getInstance()
    }

    fun getAvailabilityList(): ArrayList<String> =
            runBlocking {
                return@runBlocking GlobalScope.async {
                    return@async ArrayList<String>().apply {
                        val year = Calendar.getInstance(Locale.ENGLISH).get(Calendar.YEAR)
                        mCalendarModel?.also { calendarModel ->
                            for ((i, month) in calendarModel.months.withIndex())
                                for ((j, date) in month.days.withIndex())
                                    if (date.isSelected)
                                        add("$year-${addZeroIfNeeded(i + 1)}-${addZeroIfNeeded(j + 1)}")
                        }
                    }
                }.await()
            }

    private fun addZeroIfNeeded(month: Int) =
            month.toString().let { if (it.length <= 1) "0$it" else it}

    fun setAvailableDate(list: ArrayList<Date>) {
        mapTempDates(list)
    }

    private fun mapTempDates(list: ArrayList<Date>) = runBlocking {
        val calendarModel = GlobalScope.async {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            for (date in list) {
                calendar.time = date
                val monthModel = mCalendarModel?.months?.get(calendar.get(Calendar.MONTH))
                val day = monthModel?.days?.get(calendar.get(Calendar.DAY_OF_MONTH) - 1)
                day?.isSelected = true
            }
            return@async mCalendarModel
        }
        calendarModel.await()?.also {
            mPagerAdapter?.setData(it.months)
        }
    }
}