package com.appster.dentamatch.domain.settings

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.auth.ChangeUserLocation
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface SettingsRetrofit {

    @POST("users/user-location-update")
    fun updateUserLocation(@Body request: ChangeUserLocation): Single<BaseResponse>

    @DELETE("users/sign-out")
    fun logout(): Single<BaseResponse>
}