package com.appster.dentamatch.domain.auth

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.auth.ChangePasswordRequest
import com.appster.dentamatch.network.request.auth.LoginRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.PreferenceUtil
import com.appster.dentamatch.util.Utils
import com.google.android.gms.auth.api.Auth
import io.reactivex.Single

class AuthRemoteRepository(
        private val composer: RemoteRepositoryComposer,
        private val authRetrofit: AuthRetrofit
) {

    fun signIn(email: String,
               password: String): Single<AuthResponse> =
            authRetrofit.signIn(prepareLoginRequest(email, password))
                    .compose(composer.apiCompose())

    fun signUp(email: String,
               password: String,
               firstName: String,
               lastName: String,
               preferredJobLocationId: Int): Single<AuthResponse> =
            authRetrofit.signUp(prepareSignUpRequest(email, password, firstName, lastName, preferredJobLocationId))
                    .compose(composer.apiCompose())

    fun getPreferredJobLocation(): Single<PreferredJobLocationModel> =
            authRetrofit.getPreferredJobLocationList()
                    .compose(composer.apiCompose())

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

    fun checkUserVerified(): Single<UserVerifiedStatus> =
            authRetrofit.checkUserVerified()
                    .map {
                        it.also {
                            val userModel = PreferenceUtil.getUserModel() ?: return@also
                            userModel.isVerified = Constants.USER_VERIFIED_STATUS
                            PreferenceUtil.setUserModel(userModel)
                            PreferenceUtil.setUserVerified(Constants.USER_VERIFIED_STATUS)
                        }
                    }
                    .compose(composer.apiCompose())

    fun changePassword(confirmPassword: String, oldPassword: String, newPassword: String): Single<BaseResponse> =
            authRetrofit.changePassword(createChangePasswordRequest(confirmPassword, oldPassword, newPassword))
                    .compose(composer.apiCompose())

    private fun createChangePasswordRequest(confirmPassword: String,
                                            oldPassword: String,
                                            newPassword: String): ChangePasswordRequest =
            ChangePasswordRequest().apply {
                this.setConfirmNewPassword(confirmPassword)
                this.setOldPassword(oldPassword)
                this.setNewPassword(newPassword)
            }

    fun forgotPassword(eMail: String): Single<AuthResponse> =
            authRetrofit.forgotPassword(LoginRequest().apply { email = eMail })
                    .compose(composer.apiCompose())
}