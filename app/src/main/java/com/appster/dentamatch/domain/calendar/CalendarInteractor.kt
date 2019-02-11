package com.appster.dentamatch.domain.calendar

import android.content.Context
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse
import com.appster.dentamatch.network.response.jobs.HiredJobResponse
import com.appster.dentamatch.util.Utils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class CalendarInteractor(
        private val context: Context,
        private val remoteRepository: CalendarRemoteRepository
) {

    fun cancelJob(jobId: Int, msg: String): Completable =
            remoteRepository.cancelJob(jobId, msg)
                    .doOnSuccess { Utils.showToast(context, it.message) }
                    .ignoreElement()

    fun requestHiredJob(cal: Calendar): Single<HiredJobResponse> =
            createStartEndTime(cal).let {
                remoteRepository.requestHiredJob(it.first, it.second)
            }

    private fun createStartEndTime(calendar: Calendar): Pair<String, String> {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        val days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val date = calendar.time
        var startDate = Utils.dateFormetyyyyMMdd(date)
        startDate = startDate.substring(0, startDate.lastIndexOf("-"))
        startDate = "$startDate-01"
        val splitEndDate = Utils.dateFormetyyyyMMdd(date).split("-").dropLastWhile { it.isEmpty() }
        val endDate = "${splitEndDate[0]}-${splitEndDate[1]}-$days"
        return Pair(startDate, endDate)
    }

    fun saveAvailability(request: SaveAvailabilityRequest): Completable =
            remoteRepository.saveAvailability(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { response -> response.also { Utils.showToast(context, response.message) } }
                    .ignoreElement()

    fun requestAvailabilityList(request: GetAvailabilityRequest): Single<AvailabilityResponse> =
            remoteRepository.requestAvailabilityList(request)
}