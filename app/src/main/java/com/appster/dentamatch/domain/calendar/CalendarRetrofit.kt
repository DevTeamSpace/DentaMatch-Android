package com.appster.dentamatch.domain.calendar

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest
import com.appster.dentamatch.network.request.jobs.HiredJobRequest
import com.appster.dentamatch.network.request.tracks.CancelJobRequest
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse
import com.appster.dentamatch.network.response.jobs.HiredJobResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CalendarRetrofit {

    @POST("users/cancel-job")
    fun cancelJob(@Body createCancelJobRequest: CancelJobRequest): Single<BaseResponse>

    @POST("jobs/hired-jobs")
    fun requestHiredJob(@Body request: HiredJobRequest): Single<HiredJobResponse>

    @POST("users/update-availability")
    fun saveAvailability(@Body request: SaveAvailabilityRequest): Single<BaseResponse>

    @POST("users/availability-list")
    fun requestAvailabilityList(@Body request: GetAvailabilityRequest): Single<AvailabilityResponse>
}