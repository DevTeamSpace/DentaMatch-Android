package com.appster.dentamatch.presentation.calendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.calendar.CalendarInteractor
import com.appster.dentamatch.network.response.jobs.HiredJobResponse
import timber.log.Timber
import java.util.*

class CalendarViewModel(
        private val interactor: CalendarInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "CalendarViewModel"
    }

    private val mutableHiredJob = MutableLiveData<HiredJobResponse>()
    private val mutableCancelJob = MutableLiveData<Int>()
    private val mutableHiredJobFailed = MutableLiveData<Throwable>()

    val hiredJob: LiveData<HiredJobResponse> get() = mutableHiredJob
    val cancelJob: LiveData<Int> get() = mutableCancelJob
    val hiredJobFailed: LiveData<Throwable> get() = mutableHiredJobFailed

    fun cancelJob(jobId: Int, msg: String) =
            addDisposable(
                    interactor.cancelJob(jobId, msg)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableCancelJob.postValue(jobId) },
                                    { Timber.e(it) })
            )

    fun requestHiredJob(cal: Calendar) =
            addDisposable(
                    interactor.requestHiredJob(cal)
                            .compose(viewModelCompose())
                            .doOnError { mutableHiredJobFailed.postValue(it) }
                            .subscribe({ mutableHiredJob.postValue(it) },
                                    { Timber.e(it) })
            )
}