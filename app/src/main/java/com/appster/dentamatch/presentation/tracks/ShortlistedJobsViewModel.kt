package com.appster.dentamatch.presentation.tracks

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.domain.tracks.TracksInteractor
import com.appster.dentamatch.network.response.jobs.SearchJobResponse
import timber.log.Timber

class ShortlistedJobsViewModel(
        private val tracksInteractor: TracksInteractor,
        searchJobInteractor: SearchJobInteractor
) : SavedJobViewModel(searchJobInteractor) {

    private val mutableShortListedJobs = MutableLiveData<Pair<SearchJobResponse, Boolean>>()
    private val mutableShortListedFailed = MutableLiveData<Throwable>()
    private val mutableCancelJob = MutableLiveData<Int>()

    val shortListedJobs: LiveData<Pair<SearchJobResponse, Boolean>> get() = mutableShortListedJobs
    val shortListedFailed: LiveData<Throwable> get() = mutableShortListedFailed
    val cancelJob: LiveData<Int> get() = mutableCancelJob

    fun requestAllShortListedJobs(type: Int,
                                  page: Int,
                                  lat: Double,
                                  lng: Double,
                                  isPaginationLoading: Boolean,
                                  showProgress: Boolean) =
            addDisposable(
                    tracksInteractor.fetchTrackJobs(type, page, lat, lng)
                            .doOnSubscribe { if (showProgress) mutableLoading.postValue(true) }
                            .doFinally { mutableLoading.postValue(false) }
                            .doOnError {
                                mutableError.postValue(it)
                                mutableShortListedFailed.postValue(it)
                            }
                            .subscribe({ mutableShortListedJobs.postValue(Pair(it, isPaginationLoading)) },
                                    { Timber.e(it) })
            )

    fun cancelJob(id: Int, msg: String) =
            addDisposable(
                    tracksInteractor.cancelJob(id, msg)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableCancelJob.postValue(id) }, { Timber.e(it) })
            )
}