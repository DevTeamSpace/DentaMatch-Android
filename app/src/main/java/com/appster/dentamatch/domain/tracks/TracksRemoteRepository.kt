package com.appster.dentamatch.domain.tracks

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.tracks.CancelJobRequest
import com.appster.dentamatch.network.response.jobs.SearchJobResponse
import io.reactivex.Single

class TracksRemoteRepository(
        private val tracksRetrofit: TracksRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun cancelJob(id: Int, msg: String): Single<BaseResponse> =
            tracksRetrofit.cancelJob(createCancelJobRequest(id, msg))
                    .compose(composer.apiCompose())

    fun createCancelJobRequest(id: Int, msg: String) =
            CancelJobRequest().apply {
                setJobId(id)
                setCancelReason(msg)
            }

    fun fetchTrackJobs(type: Int, page: Int, lat: Double, lng: Double): Single<SearchJobResponse> =
            tracksRetrofit.fetchTrackJobs(type, page, lat, lng)
}