package com.appster.dentamatch.domain.profile

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.affiliation.AffiliationPostRequest
import com.appster.dentamatch.network.request.auth.LicenseRequest
import com.appster.dentamatch.network.request.certificates.CertificateRequest
import com.appster.dentamatch.network.request.profile.AboutMeRequest
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest
import com.appster.dentamatch.network.request.schools.AddSchoolRequest
import com.appster.dentamatch.network.request.skills.SkillsUpdateRequest
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest
import com.appster.dentamatch.network.request.workexp.WorkExpRequest
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse
import com.appster.dentamatch.network.response.certificates.CertificateResponse
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import com.appster.dentamatch.network.response.schools.SchoolingResponse
import com.appster.dentamatch.network.response.skills.SkillsResponse
import com.appster.dentamatch.network.response.workexp.WorkExpResponse
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

    @GET("users/affiliation-list")
    fun requestAffiliationList(): Single<AffiliationResponse>

    @POST("users/affiliation-save")
    fun saveAffiliations(@Body request: AffiliationPostRequest): Single<BaseResponse>

    @POST("users/work-experience-save")
    fun addWorkExp(@Body request: WorkExpRequest): Single<WorkExpResponse>

    @POST("users/work-experience-list")
    fun requestWorkExpList(@Body request: WorkExpListRequest): Single<WorkExpResponse>

    @GET("users/school-list")
    fun requestSchoolList(): Single<SchoolingResponse>

    @POST("users/school-add")
    fun addSchooling(@Body request: AddSchoolRequest): Single<BaseResponse>

    @GET("list-skills")
    fun requestSkillsList(): Single<SkillsResponse>

    @POST("users/update-skill")
    fun updateSkills(@Body request: SkillsUpdateRequest): Single<BaseResponse>

    @Multipart
    @POST("users/update-certificate")
    fun uploadCertificateImage(@Part("image\"; filename=\"denta_img.jpg\"") fileBody: RequestBody,
                               @Part("certificateId") typeBody: RequestBody): Single<FileUploadResponse>

    @POST("users/update-certificate-validity")
    fun saveCertificate(@Body request: CertificateRequest): Single<BaseResponse>

    @DELETE("users/work-experience-delete")
    fun deleteWorkExperience(@Query("id") id: Int): Single<BaseResponse>

    @POST("users/about-me-save")
    fun saveAboutMe(@Body request: AboutMeRequest): Single<BaseResponse>

    @GET("list-certifications")
    fun requestCertificationList(): Single<CertificateResponse>

    @POST("users/update-certificate-validity")
    fun saveCertificateList(@Body request: CertificateRequest): Single<BaseResponse>
}