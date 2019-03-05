package com.appster.dentamatch.presentation.calendar

import android.os.Parcelable
import com.appster.dentamatch.network.response.jobs.HiredJobs
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarDayModel(
        var isPartTime: Boolean = false,
        var isTemporary: Boolean = false,
        var isSelected: Boolean = false,
        val text: String,
        val jobs: ArrayList<HiredJobs> = ArrayList()
) : Parcelable