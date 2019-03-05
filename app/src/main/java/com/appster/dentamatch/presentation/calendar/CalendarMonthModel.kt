package com.appster.dentamatch.presentation.calendar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarMonthModel(
        var offset: Int = 0,
        val days: ArrayList<CalendarDayModel> = ArrayList(),
        var selected: CalendarDayModel? = null
) : Parcelable