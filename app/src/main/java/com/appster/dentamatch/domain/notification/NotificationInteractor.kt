package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single

class NotificationInteractor(
        private val remoteRepository: NotificationRemoteRepository
) {
    fun requestNotifications(page: Int): Single<NotificationResponse> =
            remoteRepository.requestNotifications(page)
}