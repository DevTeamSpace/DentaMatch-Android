package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.Notification.AcceptRejectInviteRequest
import com.appster.dentamatch.network.request.Notification.ReadNotificationRequest
import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NotificationRemoteRepository(
        private val notificationRetrofit: NotificationRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun requestNotifications(page: Int): Single<NotificationResponse> =
            notificationRetrofit.requestNotifications(page)
                    .compose(composer.apiCompose())

    fun deleteNotification(id: Int): Single<BaseResponse> =
            notificationRetrofit.deleteNotification(createDeleteNotificationRequest(id))
                    .subscribeOn(Schedulers.io())

    fun readNotification(id: Int): Single<BaseResponse> =
            notificationRetrofit.readNotification(createDeleteNotificationRequest(id))
                    .subscribeOn(Schedulers.io())

    private fun createDeleteNotificationRequest(id: Int) =
            ReadNotificationRequest().apply { setNotificationId(id) }

    private fun createAcceptNotificationRequest(id: Int, status: Int, dates: ArrayList<String>? = null) =
            AcceptRejectInviteRequest().apply {
                notificationId = id
                acceptStatus = status
                jobDates = dates
            }

    fun rejectNotification(notificationId: Int): Single<BaseResponse> =
            notificationRetrofit.acceptRejectNotification(createAcceptNotificationRequest(notificationId, 0))
                    .subscribeOn(Schedulers.io())

    fun acceptNotification(notificationId: Int, dates: java.util.ArrayList<String>): Single<BaseResponse> =
            notificationRetrofit.acceptRejectNotification(createAcceptNotificationRequest(notificationId, 1, dates))
                    .subscribeOn(Schedulers.io())
}