package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single

class NotificationRemoteRepository(
        private val notificationRetrofit: NotificationRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun requestNotifications(page: Int): Single<NotificationResponse> =
            notificationRetrofit.requestNotifications(page)
                    .compose(composer.apiCompose())
}