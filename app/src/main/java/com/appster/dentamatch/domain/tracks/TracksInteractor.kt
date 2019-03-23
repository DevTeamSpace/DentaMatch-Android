package com.appster.dentamatch.domain.tracks

import android.content.Context
import com.appster.dentamatch.network.response.jobs.SearchJobResponse
import com.appster.dentamatch.util.Utils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class TracksInteractor(
        private val tracksRemoteRepository: TracksRemoteRepository,
        private val context: Context
) {

    fun cancelJob(id: Int, msg: String): Completable =
            tracksRemoteRepository.cancelJob(id, msg)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }
                    .ignoreElement()

    fun fetchTrackJobs(type: Int, page: Int, lat: Double, lng: Double): Single<SearchJobResponse> =
            tracksRemoteRepository.fetchTrackJobs(type, page, lat, lng)
}