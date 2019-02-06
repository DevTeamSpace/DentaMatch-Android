package com.appster.dentamatch.domain.profile

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.auth.LicenseRequest
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface ProfileRetrofit {

    @GET("users/user-profile")
    fun requestProfile(): Single<ProfileResponse>

    @PUT("users/user-profile-update")
    fun updateUserProfile(@Body createProfileUpdateRequest: UpdateUserProfileRequest): Single<BaseResponse>

    @Multipart
    @POST("users/upload-image")
    fun uploadImage(@Part("type") type: RequestBody,
                    @Part("image\"; filename=\"denta_img.jpg\"") file: RequestBody): Single<FileUploadResponse>

    @PUT("users/update-license")
    fun updateLicense(@Body licenseRequest: LicenseRequest): Single<LicenseUpdateResponse>

    @GET("list-jobtitle")
    fun requestJobTitle(): Single<JobTitleResponse>

    @GET("users/unread-notification")
    fun requestUnreadNotificationCount(): Single<UnReadNotificationCountResponse>
}