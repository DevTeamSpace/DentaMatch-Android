package com.appster.dentamatch.presentation.tracks

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.domain.tracks.TracksInteractor
import timber.log.Timber

class AppliedJobsViewModel(
        private val tracksInteractor: TracksInteractor,
        private val searchJobInteractor: SearchJobInteractor
) : SavedJobViewModel(searchJobInteractor) {

    private val mutableCancelJob = MutableLiveData<Int>()

    val cancelJob: LiveData<Int> get() = mutableCancelJob

    fun cancelJob(id: Int, msg: String) =
            addDisposable(
                    tracksInteractor.cancelJob(id, msg)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableCancelJob.postValue(id) }, { Timber.e(it) })
            )
}