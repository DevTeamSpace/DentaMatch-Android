package com.appster.dentamatch.domain.notification

import android.content.Context
import android.widget.Toast
import com.appster.dentamatch.R
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList

class NotificationInteractor(
        private val remoteRepository: NotificationRemoteRepository,
        private val context: Context
) {
    fun requestNotifications(page: Int): Single<NotificationResponse> =
            remoteRepository.requestNotifications(page)

    fun deleteNotification(id: Int): Single<BaseResponse> =
            remoteRepository.deleteNotification(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { showNotificationToast(it) }

    fun readNotification(id: Int): Single<BaseResponse> =
            remoteRepository.readNotification(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { showNotificationToast(it) }

    private fun showNotificationToast(it: BaseResponse?) {
        if (it != null && it.status != 1) {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).apply {
                view.apply {
                    setBackgroundResource(R.drawable.taost_drawable)
                    setPadding(60, 60, 60, 60)
                }
            }.show()
        }
    }

    fun rejectNotification(notificationId: Int): Single<BaseResponse> =
            remoteRepository.rejectNotification(notificationId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { showNotificationToast(it) }

    fun acceptNotification(notificationId: Int, dates: ArrayList<String>): Single<BaseResponse> =
            remoteRepository.acceptNotification(notificationId, dates)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { showNotificationToast(it) }
}