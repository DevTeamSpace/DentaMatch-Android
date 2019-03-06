package com.appster.dentamatch.domain.calendar

import android.content.Context
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse
import com.appster.dentamatch.network.response.jobs.HiredJobResponse
import com.appster.dentamatch.presentation.calendar.CalendarDayModel
import com.appster.dentamatch.presentation.calendar.CalendarModel
import com.appster.dentamatch.presentation.calendar.CalendarMonthModel
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.Utils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class CalendarInteractor(
        private val context: Context,
        private val remoteRepository: CalendarRemoteRepository
) {

    fun cancelJob(jobId: Int, msg: String, calendarModel: CalendarModel?): Single<CalendarModel> =
            remoteRepository.cancelJob(jobId, msg)
                    .doOnSuccess { Utils.showToast(context, it.message) }
                    .map { return@map removeJobFromCalendarModel(calendarModel, jobId) }

    private fun removeJobFromCalendarModel(calendarModel: CalendarModel?, jobId: Int) =
            calendarModel?.apply {
                for (month in calendarModel.months) {
                    for (day in month.days) {
                        for ((i, hiredJob) in day.jobs.withIndex()) {
                            if (hiredJob.id == jobId) {
                                day.jobs.removeAt(i)
                                break
                            }
                        }
                    }
                }
            }

    fun requestHiredJob(cal: Calendar, calendarModel: CalendarModel): Single<CalendarModel> =
            createStartEndTime(cal).let {
                remoteRepository.requestHiredJob(it.first, it.second)
                        .map { response -> return@map wrapHiredJobs(response, calendarModel) }
            }

    private fun wrapHiredJobs(response: HiredJobResponse, calendarModel: CalendarModel) =
            calendarModel.apply {
                fillWithData(this, response)
            }

    private fun fillWithData(model: CalendarModel, response: HiredJobResponse) {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        for (job in response.hiredJobResponseData.jobList) {
            calendar.time = Utils.getDate(job.jobDate, Constants.DateFormat.YYYYMMDD)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val calendarDayModel = model.months[month].days[day - 1]
            calendarDayModel.jobs.add(job)
            when {
                job.jobType == Constants.JOBTYPE.FULL_TIME.value -> model.isFullTime = true
                job.jobType == Constants.JOBTYPE.PART_TIME.value -> calendarDayModel.isPartTime = true
                job.jobType == Constants.JOBTYPE.TEMPORARY.value -> calendarDayModel.isTemporary = true
            }
        }
    }

    private fun createStartEndTime(calendar: Calendar): Pair<String, String> {
        val year = calendar.get(Calendar.YEAR)
        return Pair("$year-01-01", "$year-12-31")
    }

    fun saveAvailability(request: SaveAvailabilityRequest): Completable =
            remoteRepository.saveAvailability(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { response -> response.also { Utils.showToast(context, response.message) } }
                    .ignoreElement()

    fun requestAvailabilityList(request: GetAvailabilityRequest): Single<AvailabilityResponse> =
            remoteRepository.requestAvailabilityList(request)
}