package com.appster.dentamatch.domain.profile

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import com.appster.dentamatch.util.Constants
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class ProfileInteractor(
        private val profileRemoteRepository: ProfileRemoteRepository
) {

    fun requestProfile(): Single<ProfileResponse> = profileRemoteRepository.requestProfile()

    fun updateUserProfile(firstName: String,
                          lastName: String,
                          about: String,
                          jobTitleId: Int,
                          preferredJobLocationId: String,
                          state: String?,
                          license: String?): Single<BaseResponse> =
            profileRemoteRepository.updateUserProfile(firstName,
                    lastName,
                    about,
                    jobTitleId,
                    preferredJobLocationId,
                    state,
                    license)

    fun uploadImage(filePath: String): Single<FileUploadResponse> =
            profileRemoteRepository.uploadImage(createImageTypeBody(), createImageFileBody(filePath))

    private fun createImageFileBody(filePath: String): RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(filePath))

    private fun createImageTypeBody(): RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), Constants.APIS.IMAGE_TYPE_PIC)

    fun updateLicense(jobTitleId: Int, about: String, license: String?, state: String?): Single<LicenseUpdateResponse> =
            profileRemoteRepository.updateLicense(jobTitleId, about, license, state)

    fun requestJobTitle(): Single<JobTitleResponse> = profileRemoteRepository.requestJobTitle()

    fun requestUnreadNotificationCount(): Single<UnReadNotificationCountResponse> =
            profileRemoteRepository.requestUnreadNotificationCount()
}