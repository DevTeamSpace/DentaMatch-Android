package com.appster.dentamatch.presentation.searchjob

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.notification.NotificationInteractor
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.network.response.jobs.JobDetailResponse
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.util.ArrayList

class JobDetailViewModel(
        private val searchJobInteractor: SearchJobInteractor,
        private val notificationInteractor: NotificationInteractor
) : BaseLoadingViewModel() {

    private val mutableApplyJobResponse = MutableLiveData<BaseResponse>()
    private val mutableJobDetailResponse = MutableLiveData<JobDetailResponse>()
    private val mutableSaveUnSave = MutableLiveData<Pair<Int, Int>>()
    private val mutableFailedSaveUnSave = MutableLiveData<Int>()

    val applyJobResponse: LiveData<BaseResponse> get() = mutableApplyJobResponse
    val jobDetailResponse: LiveData<JobDetailResponse> get() = mutableJobDetailResponse
    val saveUnSave: LiveData<Pair<Int, Int>> get() = mutableSaveUnSave
    val failedSaveUnSave: LiveData<Int> get() = mutableFailedSaveUnSave

    fun applyJob(jobId: Int) {
        addDisposable(
                searchJobInteractor.applyJob(jobId)
                        .compose(viewModelCompose())
                        .subscribe({ mutableApplyJobResponse.postValue(it) }, { Timber.e(it) })
        )
    }

    fun requestJobDetail(jobId: Int) =
            addDisposable(
                    searchJobInteractor.requestJobDetail(jobId)
                            .compose(viewModelCompose())
                            .subscribe({ mutableJobDetailResponse.postValue(it) }, { Timber.e(it) })
            )

    fun saveUnSaveJob(jobId: Int, status: Int) =
            addDisposable(
                    searchJobInteractor.saveUnSaveJob(jobId, status)
                            .compose(viewModelCompletableCompose())
                            .doOnError { mutableFailedSaveUnSave.postValue(status) }
                            .subscribe({ mutableSaveUnSave.postValue(Pair(jobId, status)) }, { Timber.e(it) })
            )

    fun acceptJobInvite(notificationId: Int, dates: @NotNull ArrayList<String>) =
            addDisposable(
                    notificationInteractor.acceptNotification(notificationId, dates)
                            .compose(viewModelCompose())
                            .subscribe({ mutableApplyJobResponse.postValue(it) }, { Timber.e(it) })
            )
}