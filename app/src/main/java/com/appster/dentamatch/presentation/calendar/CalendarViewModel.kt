package com.appster.dentamatch.presentation.calendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.calendar.CalendarInteractor
import timber.log.Timber
import java.util.*

class CalendarViewModel(
        private val interactor: CalendarInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "CalendarViewModel"
    }

    private val mutableCalendarModel = MutableLiveData<CalendarModel>()
    private val mutableCancelJob = MutableLiveData<CalendarModel>()
    private val mutableHiredJobFailed = MutableLiveData<Throwable>()

    val calendarModel: LiveData<CalendarModel> get() = mutableCalendarModel
    val cancelJob: LiveData<CalendarModel> get() = mutableCancelJob
    val hiredJobFailed: LiveData<Throwable> get() = mutableHiredJobFailed

    fun cancelJob(jobId: Int, msg: String, calendarModel: CalendarModel?) =
            addDisposable(
                    interactor.cancelJob(jobId, msg, calendarModel)
                            .compose(viewModelCompose())
                            .subscribe({ mutableCancelJob.postValue(it) },
                                    { Timber.e(it) })
            )

    fun requestHiredJob(cal: Calendar, calendarModel: CalendarModel) =
            addDisposable(
                    interactor.requestHiredJob(cal, calendarModel)
                            .compose(viewModelCompose())
                            .doOnError { mutableHiredJobFailed.postValue(it) }
                            .subscribe({ mutableCalendarModel.postValue(it) },
                                    { Timber.e(it) })
            )
}