package com.appster.dentamatch.domain.login

import com.appster.dentamatch.network.request.auth.LoginRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginRetrofit {

    @POST("users/sign-up")
    fun signUp(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("users/sign-in")
    fun signIn(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @GET("jobs/preferred-job-locations")
    fun getPreferredJobLocationList(): Single<PreferredJobLocationModel>
}