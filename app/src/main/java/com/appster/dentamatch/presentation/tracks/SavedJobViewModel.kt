package com.appster.dentamatch.presentation.tracks

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.presentation.searchjob.JobListViewModel
import timber.log.Timber

open class SavedJobViewModel(
        private val searchJobInteractor: SearchJobInteractor
) : BaseLoadingViewModel() {

    private val mutableUnSaveJob = MutableLiveData<JobListViewModel.SaveUnSaveJobResult>()
    private val mutableUnSaveJobFailed = MutableLiveData<JobListViewModel.SaveUnSaveJobResult>()

    val unSaveJob: LiveData<JobListViewModel.SaveUnSaveJobResult> get() = mutableUnSaveJob
    val unSaveJobFailed: LiveData<JobListViewModel.SaveUnSaveJobResult> get() = mutableUnSaveJobFailed

    fun unSaveJob(id: Int, position: Int) =
            addDisposable(
                    searchJobInteractor.saveUnSaveJob(id, 0)
                            .compose(viewModelCompletableCompose())
                            .doOnError { mutableUnSaveJobFailed.postValue(JobListViewModel.SaveUnSaveJobResult(0, position)) }
                            .subscribe({ mutableUnSaveJob.postValue(JobListViewModel.SaveUnSaveJobResult(0, position)) },
                                    { Timber.e(it) })
            )
}