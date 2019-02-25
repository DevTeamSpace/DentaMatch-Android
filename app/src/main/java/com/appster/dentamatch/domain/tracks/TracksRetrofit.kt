package com.appster.dentamatch.domain.tracks

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.tracks.CancelJobRequest
import com.appster.dentamatch.network.response.jobs.SearchJobResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TracksRetrofit {

    @POST("users/cancel-job")
    fun cancelJob(@Body request: CancelJobRequest): Single<BaseResponse>

    @GET("users/job-list")
    fun fetchTrackJobs(@Query("type") type: Int,
                       @Query("page") page: Int,
                       @Query("lat") lat: Double,
                       @Query("lng") lng: Double): Single<SearchJobResponse>
}