package com.appster.dentamatch.domain.calendar

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest
import com.appster.dentamatch.network.request.jobs.HiredJobRequest
import com.appster.dentamatch.network.request.tracks.CancelJobRequest
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse
import com.appster.dentamatch.network.response.jobs.HiredJobResponse
import io.reactivex.Single

class CalendarRemoteRepository(
        private val retrofit: CalendarRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun cancelJob(jobId: Int, msg: String): Single<BaseResponse> =
            retrofit.cancelJob(createCancelJobRequest(jobId, msg))
                    .compose(composer.apiCompose())

    private fun createCancelJobRequest(jobId: Int, msg: String): CancelJobRequest =
            CancelJobRequest().apply {
                setJobId(jobId)
                setCancelReason(msg)
            }

    fun requestHiredJob(startDate: String, endDate: String): Single<HiredJobResponse> =
            retrofit.requestHiredJob(createHiredJobRequest(startDate, endDate))
                    .compose(composer.apiCompose())

    private fun createHiredJobRequest(startDate: String, endDate: String) =
            HiredJobRequest().apply {
                setJobStartDate(startDate)
                setJobEndDate(endDate)
            }

    fun saveAvailability(request: SaveAvailabilityRequest): Single<BaseResponse> =
            retrofit.saveAvailability(request)
                    .compose(composer.apiCompose())

    fun requestAvailabilityList(request: GetAvailabilityRequest): Single<AvailabilityResponse> =
            retrofit.requestAvailabilityList(request)
                    .compose(composer.apiCompose())
}