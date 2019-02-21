package com.appster.dentamatch.presentation.searchjob

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.network.response.jobs.SearchJobModel
import timber.log.Timber

class JobMapViewModel(
        private val searchJobInteractor: SearchJobInteractor
) : BaseLoadingViewModel() {

    private val mutableSaveUnSaveJob = MutableLiveData<Int>()
    private val mutableSaveUnSaveJobFailed = MutableLiveData<Int>()

    val saveUnSaveJob: LiveData<Int> get() = mutableSaveUnSaveJob
    val saveUnSaveJobFailed: LiveData<Int> get() = mutableSaveUnSaveJobFailed

    fun saveUnSaveJob(jobId: Int, status: Int, model: SearchJobModel) =
            addDisposable(
                    searchJobInteractor.saveUnSaveJob(jobId, status)
                            .compose(viewModelCompletableCompose())
                            .doOnComplete {
                                model.isSaved = status
                                SearchJobDataHelper.getInstance().notifyItemsChanged(model)
                            }
                            .doOnError { mutableSaveUnSaveJobFailed.postValue(status) }
                            .subscribe({ mutableSaveUnSaveJob.postValue(status) }, { Timber.e(it) })
            )


}