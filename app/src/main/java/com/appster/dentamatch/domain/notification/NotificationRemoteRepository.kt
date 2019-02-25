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

    fun acceptRejectNotification(id: Int, status: Int): Single<BaseResponse> =
            notificationRetrofit.acceptRejectNotification(createAcceptNotificationRequest(id, status))
                    .subscribeOn(Schedulers.io())

    fun readNotification(id: Int): Single<BaseResponse> =
            notificationRetrofit.readNotification(createDeleteNotificationRequest(id))
                    .subscribeOn(Schedulers.io())

    private fun createDeleteNotificationRequest(id: Int) =
            ReadNotificationRequest().apply { setNotificationId(id) }

    private fun createAcceptNotificationRequest(id: Int, status: Int) =
            AcceptRejectInviteRequest().apply {
                setNotificationId(id)
                setAcceptStatus(status)
            }
}