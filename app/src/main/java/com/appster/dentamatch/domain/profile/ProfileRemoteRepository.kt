package com.appster.dentamatch.domain.profile

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.auth.LicenseRequest
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import com.appster.dentamatch.util.PreferenceUtil
import io.reactivex.Single
import okhttp3.RequestBody

class ProfileRemoteRepository(
        private val composer: RemoteRepositoryComposer,
        private val profileRetrofit: ProfileRetrofit
) {

    fun requestProfile(): Single<ProfileResponse> = profileRetrofit.requestProfile()
            .map { it.apply { PreferenceUtil.setJobTitleList(it.profileResponseData.jobTitleLists) } }
            .compose(composer.apiCompose())

    fun updateUserProfile(firstName: String,
                          lastName: String,
                          about: String,
                          jobTitleId: Int,
                          preferredJobLocationId: String,
                          state: String?,
                          license: String?): Single<BaseResponse> =
            profileRetrofit.updateUserProfile(
                    createProfileUpdateRequest(firstName,
                            lastName,
                            about,
                            jobTitleId,
                            preferredJobLocationId,
                            state,
                            license)
            )
                    .compose(composer.apiCompose())

    fun uploadImage(type: RequestBody,
                    file: RequestBody): Single<FileUploadResponse> =
            profileRetrofit.uploadImage(type, file)
                    .map {
                        it.apply {
                            if (it.status == 1) PreferenceUtil.setProfileImagePath(it.fileUploadResponseData.imageUrl)
                        }
                    }
                    .compose(composer.apiCompose())

    private fun createProfileUpdateRequest(firstName: String,
                                           lastName: String,
                                           about: String,
                                           jobTitleId: Int,
                                           preferredJobLocationId: String,
                                           state: String?,
                                           license: String?) =
            UpdateUserProfileRequest().apply {
                this.firstName = firstName
                this.lastName = lastName
                this.aboutMe = about
                this.jobTitleID = jobTitleId
                this.preferredJobLocationId = preferredJobLocationId
                this.state = state
                this.licenseNumber = license
            }

    fun updateLicense(jobTitleId: Int, about: String, license: String?, state: String?): Single<LicenseUpdateResponse> =
            profileRetrofit.updateLicense(createUpdateLicenseRequest(jobTitleId, about, license, state))
                    .doOnError {
                        PreferenceUtil.setJobTitleId(0)
                    }
                    .doOnSuccess {
                        PreferenceUtil.setUserVerified(it.result.userDetails.isVerified)
                        PreferenceUtil.setUserModel(it.result.userDetails)
                        PreferenceUtil.setUserToken(it.result.userDetails.accessToken)
                        PreferenceUtil.setJobTitleId(jobTitleId)
                    }
                    .compose(composer.apiCompose())

    private fun createUpdateLicenseRequest(jobTitleId: Int, about: String, license: String?, state: String?): LicenseRequest =
            LicenseRequest().apply {
                this.jobTitleId = jobTitleId
                this.aboutMe = about
                this.license = license
                this.state = state
            }

    fun requestJobTitle(): Single<JobTitleResponse> =
            profileRetrofit.requestJobTitle()
                    .doOnSuccess { PreferenceUtil.setJobTitleList(it.jobTitleResponseData.jobTitleList) }
                    .compose(composer.apiCompose())

    fun requestUnreadNotificationCount(): Single<UnReadNotificationCountResponse> =
            profileRetrofit.requestUnreadNotificationCount()
                    .compose(composer.apiCompose())
}