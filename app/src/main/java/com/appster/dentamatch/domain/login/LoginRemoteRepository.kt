package com.appster.dentamatch.domain.login

import com.appster.dentamatch.base.ApiErrorHandler
import com.appster.dentamatch.base.BaseRemoteRepository
import com.appster.dentamatch.network.request.auth.LoginRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.Utils
import io.reactivex.Single

class LoginRemoteRepository(
        apiErrorHandler: ApiErrorHandler,
        private val loginRetrofit: LoginRetrofit
) : BaseRemoteRepository(apiErrorHandler) {

    fun signIn(email: String,
               password: String): Single<LoginResponse> =
            loginRetrofit.signIn(prepareLoginRequest(email, password))
                    .compose(apiCompose())

    fun signUp(email: String,
               password: String,
               firstName: String,
               lastName: String,
               preferredJobLocationId: Int): Single<LoginResponse> =
            loginRetrofit.signUp(prepareSignUpRequest(email, password, firstName, lastName, preferredJobLocationId))
                    .compose(apiCompose())

    fun getPreferredJobLocation(): Single<PreferredJobLocationModel> =
            loginRetrofit.getPreferredJobLocationList()
                    .compose(apiCompose())

    private fun prepareSignUpRequest(email: String,
                                     password: String,
                                     firstName: String,
                                     lastName: String,
                                     preferredJobLocationId: Int): LoginRequest =
        LoginRequest().apply {
            this.deviceId = Utils.getDeviceID()
            this.deviceToken = Utils.getDeviceToken()
            this.deviceType = Constants.DEVICE_TYPE
            this.email = email
            this.password = password
            this.firstName = firstName
            this.lastName = lastName
            this.preferredJobLocationId = preferredJobLocationId
        }


    private fun prepareLoginRequest(email: String,
                                    password: String): LoginRequest =
        LoginRequest().apply {
            this.deviceId = Utils.getDeviceID()
            this.deviceToken = Utils.getDeviceToken()
            this.deviceType = Constants.DEVICE_TYPE
            this.email = email
            this.password = password
        }
}