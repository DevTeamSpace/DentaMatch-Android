package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.jobs.JobApplyRequest
import com.appster.dentamatch.network.request.jobs.JobDetailRequest
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.jobs.JobDetailResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.util.PreferenceUtil
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchJobRemoteRepository(
        private val composer: RemoteRepositoryComposer,
        private val searchJobRetrofit: SearchJobRetrofit
) {

    fun requestJobTitles(): Single<JobTitleResponse> =
            searchJobRetrofit.jobTitles()
                    .compose(composer.apiCompose())

    fun requestPreferredJobLocationList(): Single<PreferredJobLocationModel> =
            searchJobRetrofit.preferredJobLocationList()
                    .map { it.also { list -> PreferenceUtil.setPreferredJobList(list.result.preferredJobLocations) } }
                    .compose(composer.apiCompose())

    fun applyJob(jobId: Int): Single<BaseResponse> =
            searchJobRetrofit.applyJob(JobApplyRequest().apply { setJobId(jobId) })
                    .subscribeOn(Schedulers.io())

    fun requestJobDetail(jobId: Int): Single<JobDetailResponse> =
            searchJobRetrofit.requestJobDetail(JobDetailRequest().apply { setJobId(jobId) })
                    .subscribeOn(Schedulers.io())

    fun saveUnSaveJob(jobId: Int, status: Int): Single<BaseResponse> =
            searchJobRetrofit.saveUnSaveJob(createSaveUnSaveRequest(jobId, status))
                    .compose(composer.apiCompose())

    private fun createSaveUnSaveRequest(jobId: Int, status: Int) =
        SaveUnSaveRequest().apply {
            setJobId(jobId)
            setStatus(status)
        }
}