package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.Notification.AcceptRejectInviteRequest
import com.appster.dentamatch.network.request.Notification.ReadNotificationRequest
import com.appster.dentamatch.network.response.notification.NotificationResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationRetrofit {

    @GET("users/notification-list")
    fun requestNotifications(@Query("page") page: Int): Single<NotificationResponse>

    @POST("users/delete-notification")
    fun deleteNotification(@Body request: ReadNotificationRequest): Single<BaseResponse>

    @POST("users/acceptreject-job")
    fun acceptRejectNotification(@Body request: AcceptRejectInviteRequest): Single<BaseResponse>

    @POST("users/notification-read")
    fun readNotification(id: ReadNotificationRequest): Single<BaseResponse>
}