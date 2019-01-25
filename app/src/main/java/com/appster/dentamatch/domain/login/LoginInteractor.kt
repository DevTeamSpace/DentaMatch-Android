package com.appster.dentamatch.domain.login

import android.text.TextUtils
import com.appster.dentamatch.model.UserModel
import com.appster.dentamatch.network.request.jobs.SearchJobRequest
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.util.PreferenceUtil
import com.crashlytics.android.Crashlytics
import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.reactivex.Single
import org.json.JSONObject
import java.util.ArrayList

class LoginInteractor(private val remoteRepository: LoginRemoteRepository,
                      private val mixpanelAPI: MixpanelAPI) {

    companion object {
        const val EVENT_LOGIN = "Login"
        const val EVENT_SIGHUP = "SignUp"
        const val NAME_LABEL = "Name"
        const val EMAIL_LABEL = "Email"
        const val STATUS_SUCCESS = 1
    }

    fun signIn(email: String, password: String): Single<LoginResponse> =
            remoteRepository.signIn(email, password)
                    .map { logMixpanelEvent(it) }
                    .map { mapSearchJobFilter(it) }
                    .map { loginUser(it) }
                    .map { saveUserModel(it) }

    private fun logMixpanelEvent(loginResponse: LoginResponse): LoginResponse {
        if (loginResponse.status == STATUS_SUCCESS) {
            val userDetails = JSONObject()
            val userDetail = loginResponse.loginResponseData.userDetail
            userDetails.put(NAME_LABEL, "${userDetail.firstName} ${userDetail.lastName}")
            userDetails.put(EMAIL_LABEL, userDetail.email)
            mixpanelAPI.track(EVENT_LOGIN, userDetails)
        }
        return loginResponse
    }

    private fun mapSearchJobFilter(loginResponse: LoginResponse): LoginResponse {
        val loginResponseData = loginResponse.loginResponseData
        if (loginResponse.status == STATUS_SUCCESS && loginResponseData.searchFilters != null) {
            val searchFilters = loginResponseData.searchFilters
            val request = SearchJobRequest()
            if (searchFilters.isParttime != null && !TextUtils.isEmpty(searchFilters.isParttime)) {
                request.isParttime = Integer.parseInt(searchFilters.isParttime)
            }
            if (searchFilters.isFulltime != null && !TextUtils.isEmpty(searchFilters.isFulltime)) {
                request.isFulltime = Integer.parseInt(searchFilters.isFulltime)
            }

            request.lat = searchFilters.lat
            request.lng = searchFilters.lng
            request.jobTitle = searchFilters.jobTitle
            request.page = 1

            if (searchFilters.parttimeDays != null && searchFilters.parttimeDays.size > 0) {
                request.parttimeDays = searchFilters.parttimeDays
            } else {
                request.parttimeDays = ArrayList<String>()
            }

            request.country = searchFilters.country
            request.city = searchFilters.city
            request.state = searchFilters.state

            request.zipCode = searchFilters.zipCode
            request.selectedJobTitles = searchFilters.selectedJobTitles
            request.address = searchFilters.address

            /**
             * This value is set in order to redirect user from signIn or splash screen.
             */
            PreferenceUtil.setJobFilter(true)
            PreferenceUtil.saveJobFilter(request)
        }
        return loginResponse
    }

    private fun persistUserData(loginResponse: LoginResponse) {
        val userDetail = loginResponse.loginResponseData.userDetail
        PreferenceUtil.setUserToken(userDetail.userToken)
        PreferenceUtil.setFistName(userDetail.firstName)
        PreferenceUtil.setLastName(userDetail.lastName)
        PreferenceUtil.setProfileImagePath(userDetail.imageUrl)
        PreferenceUtil.setUserChatId(userDetail.id)
        PreferenceUtil.setPreferredJobLocationName(userDetail.preferredLocationName)
        PreferenceUtil.setPreferredJobLocationID(userDetail.preferredJobLocationId)
        PreferenceUtil.setLicenseNumber(userDetail.licenseNumber)
        PreferenceUtil.setKeyJobSeekerVerified(userDetail.isJobSeekerVerified)
        PreferenceUtil.setUserVerified(userDetail.isVerified)
    }

    private fun loginUser(loginResponse: LoginResponse): LoginResponse {
        if (loginResponse.status == STATUS_SUCCESS) {
            val userDetail = loginResponse.loginResponseData.userDetail
            PreferenceUtil.setIsLogin(true)

            persistUserData(loginResponse)

            if (userDetail != null && userDetail.jobTitileId != null) {
                PreferenceUtil.setJobTitleId(userDetail.jobTitileId)
            }

            Crashlytics.setUserIdentifier(userDetail.id)
            Crashlytics.setUserEmail(userDetail.email)
            Crashlytics.setUserName("${userDetail.firstName} ${userDetail.lastName}")
        }
        return loginResponse
    }

    private fun saveUserModel(loginResponse: LoginResponse): LoginResponse {
        if (loginResponse.status == STATUS_SUCCESS) {
            val userModel = UserModel()
            val userDetail = loginResponse.loginResponseData.userDetail

            userModel.email = userDetail.email
            userModel.firstName = userDetail.firstName
            userModel.lastName = userDetail.firstName
            userModel.profileCompleted = userDetail.profileCompleted
            userModel.userId = Integer.parseInt(userDetail.id)
            userModel.isVerified = userDetail.isVerified
            userModel.isJobSeekerVerified = userDetail.isJobSeekerVerified
            userModel.isJobSeekerVerified = userDetail.isJobSeekerVerified

            if (userDetail.jobTitileId != null) {
                userModel.jobTitleId = userDetail.jobTitileId
            }
            userModel.isVerified = userDetail.isVerified
            PreferenceUtil.setUserModel(userModel)
            PreferenceUtil.setSetAvailability(true)
        }
        return loginResponse
    }

    fun signUp(email: String,
               password: String,
               firstName: String,
               lastName: String,
               preferredJobLocationId: Int): Single<LoginResponse> =
            remoteRepository.signUp(email, password, firstName, lastName, preferredJobLocationId)
                    .map { signUpUser(it) }

    private fun signUpUser(loginResponse: LoginResponse): LoginResponse {
        if (loginResponse.status == STATUS_SUCCESS) {
            mixpanelAPI.track(EVENT_SIGHUP)
            persistUserData(loginResponse)
        }
        return loginResponse
    }

    fun getPreferred(): Single<PreferredJobLocationModel> = remoteRepository.getPreferredJobLocation()
}