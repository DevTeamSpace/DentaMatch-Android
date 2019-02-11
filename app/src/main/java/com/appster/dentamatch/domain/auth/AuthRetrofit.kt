package com.appster.dentamatch.domain.auth

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.auth.ChangePasswordRequest
import com.appster.dentamatch.network.request.auth.LoginRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthRetrofit {

    @POST("users/sign-up")
    fun signUp(@Body loginRequest: LoginRequest): Single<AuthResponse>

    @POST("users/sign-in")
    fun signIn(@Body loginRequest: LoginRequest): Single<AuthResponse>

    @GET("jobs/preferred-job-locations")
    fun getPreferredJobLocationList(): Single<PreferredJobLocationModel>

    @GET("users/is-verified")
    fun checkUserVerified(): Single<UserVerifiedStatus>

    @POST("users/change-password")
    fun changePassword(@Body createChangePasswordRequest: ChangePasswordRequest): Single<BaseResponse>

    @PUT("users/forgot-password")
    fun forgotPassword(@Body createForgotPasswordRequest: LoginRequest): Single<AuthResponse>
}