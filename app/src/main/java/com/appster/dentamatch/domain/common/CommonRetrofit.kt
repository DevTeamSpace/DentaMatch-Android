package com.appster.dentamatch.domain.common

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.Notification.UpdateFcmTokenRequest
import com.appster.dentamatch.network.response.profile.StateResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommonRetrofit {

    @POST("users/update-devicetoken")
    fun updateFcmToken(@Body tokenRequest: UpdateFcmTokenRequest): Single<BaseResponse>

    @GET("list-states")
    fun requestStateList(): Single<StateResponse>
}