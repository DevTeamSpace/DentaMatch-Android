package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.jobs.JobApplyRequest
import com.appster.dentamatch.network.request.jobs.JobDetailRequest
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.jobs.JobDetailResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SearchJobRetrofit {

    @GET("list-jobtitle")
    fun jobTitles(): Single<JobTitleResponse>

    @GET("jobs/preferred-job-locations")
    fun preferredJobLocationList(): Single<PreferredJobLocationModel>

    @POST("users/apply-job")
    fun applyJob(@Body request: JobApplyRequest): Single<BaseResponse>

    @POST("jobs/job-detail")
    fun requestJobDetail(@Body apply: JobDetailRequest): Single<JobDetailResponse>

    @POST("users/save-job")
    fun saveUnSaveJob(@Body request: SaveUnSaveRequest): Single<BaseResponse>
}