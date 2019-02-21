package com.appster.dentamatch.presentation.searchjob

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import timber.log.Timber

class JobListViewModel(
        private val searchJobInteractor: SearchJobInteractor
) : BaseLoadingViewModel() {

    private val mutableSaveUnSaveJob = MutableLiveData<SaveUnSaveJobResult>()

    val saveUnSaveJob: LiveData<SaveUnSaveJobResult> get() = mutableSaveUnSaveJob

    fun saveUnSaveJob(jobID: Int, status: Int, position: Int) =
            addDisposable(
                    searchJobInteractor.saveUnSaveJob(jobID, status)
                            .compose(viewModelCompletableCompose())
                            .doOnError { mutableSaveUnSaveJob.postValue(SaveUnSaveJobResult(invertStatus(status), position)) }
                            .subscribe({ mutableSaveUnSaveJob.postValue(SaveUnSaveJobResult(status, position)) },
                                    { Timber.e(it) })
            )

    private fun invertStatus(status: Int) =
            status.let {
                if (it == 1) {
                    return@let 0
                } else {
                    return@let 1
                }
            }

    data class SaveUnSaveJobResult(
            val status: Int,
            val position: Int
    )
}