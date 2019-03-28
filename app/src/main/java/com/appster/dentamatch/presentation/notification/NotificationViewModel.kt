package com.appster.dentamatch.presentation.notification

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.notification.NotificationInteractor
import com.appster.dentamatch.network.response.notification.NotificationResponse
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.util.ArrayList

class NotificationViewModel(
        private val notificationInteractor: NotificationInteractor
) : BaseLoadingViewModel() {

    private val mutableNotificationResponse = MutableLiveData<Pair<NotificationResponse, Boolean>>()
    private val mutableNotificationFailed = MutableLiveData<Throwable>()
    private val mutableReadNotification = MutableLiveData<Int>()
    private val mutableAcceptNotification = MutableLiveData<Int>()
    private val mutableDeleteNotification = MutableLiveData<Int>()

    val notificationResponse: LiveData<Pair<NotificationResponse, Boolean>> get() = mutableNotificationResponse
    val notificationFailed: LiveData<Throwable> get() = mutableNotificationFailed
    val readNotification: LiveData<Int> get() = mutableReadNotification
    val acceptNotification: LiveData<Int> get() = mutableAcceptNotification
    val deleteNotification: LiveData<Int> get() = mutableDeleteNotification

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

    fun readNotification(id: Int) =
            addDisposable(
                    notificationInteractor.readNotification(id)
                            .subscribe({ if (checkStatus(it)) mutableReadNotification.postValue(id) },
                                    { Timber.e(it) })
            )

    fun deleteNotification(id: Int) =
            addDisposable(
                    notificationInteractor.deleteNotification(id)
                            .subscribe({ if (checkStatus(it)) mutableDeleteNotification.postValue(id) },
                                    {Timber.e(it)})
            )

    private fun checkStatus(it: BaseResponse?) = it != null && it.status == 1

    fun rejectNotification(notificationId: Int) =
            addDisposable(
                    notificationInteractor.rejectNotification(notificationId)
                            .subscribe({ if (checkStatus(it)) mutableAcceptNotification.postValue(notificationId) },
                                    { Timber.e(it) })
            )

    fun acceptNotification(notificationId: Int, dates: @NotNull ArrayList<String>) =
            addDisposable(
                    notificationInteractor.acceptNotification(notificationId, dates)
                            .subscribe({ if (checkStatus(it)) mutableAcceptNotification.postValue(notificationId) },
                                    { Timber.e(it) })
            )
}