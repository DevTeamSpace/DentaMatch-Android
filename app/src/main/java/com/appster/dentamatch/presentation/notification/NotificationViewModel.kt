package com.appster.dentamatch.presentation.notification

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.notification.NotificationInteractor
import com.appster.dentamatch.network.response.notification.NotificationResponse
import timber.log.Timber

class NotificationViewModel(
        private val notificationInteractor: NotificationInteractor
) : BaseLoadingViewModel() {

    private val mutableNotificationResponse = MutableLiveData<Pair<NotificationResponse, Boolean>>()
    private val mutableNotificationFailed = MutableLiveData<Throwable>()

    val notificationResponse: LiveData<Pair<NotificationResponse, Boolean>> get() = mutableNotificationResponse
    val notificationFailed: LiveData<Throwable> get() = mutableNotificationFailed

    fun requestNotifications(page: Int, refreshing: Boolean) =
            addDisposable(
                    notificationInteractor.requestNotifications(page)
                            .doOnSubscribe { if (refreshing) mutableLoading.postValue(true) }
                            .doOnError { throwable -> mutableError.postValue(throwable) }
                            .doFinally { mutableLoading.postValue(false) }
                            .subscribe({ mutableNotificationResponse.postValue(Pair(it, refreshing)) },
                                    {
                                        Timber.e(it)
                                        mutableNotificationFailed.postValue(it)
                                    })
            )
}