package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface SearchJobRetrofit {

    @GET("list-jobtitle")
    fun jobTitles(): Single<JobTitleResponse>

    @GET("jobs/preferred-job-locations")
    fun preferredJobLocationList(): Single<PreferredJobLocationModel>

}