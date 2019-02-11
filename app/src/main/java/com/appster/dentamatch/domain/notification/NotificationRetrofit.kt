package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationRetrofit {

    @GET("users/notification-list")
    fun requestNotifications(@Query("page") page: Int): Single<NotificationResponse>
}