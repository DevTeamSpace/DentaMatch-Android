package com.appster.dentamatch.domain.notification

import android.content.Context
import android.widget.Toast
import com.appster.dentamatch.R
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class NotificationInteractor(
        private val remoteRepository: NotificationRemoteRepository,
        private val context: Context
) {
    fun requestNotifications(page: Int): Single<NotificationResponse> =
            remoteRepository.requestNotifications(page)

    fun deleteNotification(iDs: ArrayList<Int>): Single<BaseResponse> =
            remoteRepository.deleteNotification(iDs)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { showNotificationToast(it) }

    fun acceptRejectNotification(id: Int, status: Int): Single<BaseResponse> =
            remoteRepository.acceptRejectNotification(id, status)
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
}