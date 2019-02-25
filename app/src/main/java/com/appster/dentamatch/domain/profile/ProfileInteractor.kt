package com.appster.dentamatch.domain.profile

import android.content.Context
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.eventbus.LocationEvent
import com.appster.dentamatch.model.ParentSkillModel
import com.appster.dentamatch.model.SchoolTypeModel
import com.appster.dentamatch.network.request.schools.PostSchoolData
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest
import com.appster.dentamatch.network.request.workexp.WorkExpRequest
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse
import com.appster.dentamatch.network.response.certificates.CertificateResponse
import com.appster.dentamatch.network.response.certificates.CertificatesList
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import com.appster.dentamatch.network.response.schools.SchoolingResponse
import com.appster.dentamatch.network.response.skills.SkillsResponse
import com.appster.dentamatch.network.response.workexp.WorkExpResponse
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList
import java.util.HashMap

class ProfileInteractor(
        private val profileRemoteRepository: ProfileRemoteRepository,
        private val context: Context
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

    fun uploadImage(filePath: String, type: String): Single<FileUploadResponse> =
            profileRemoteRepository.uploadImage(createImageTypeBody(type), createImageFileBody(filePath))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    private fun createImageFileBody(filePath: String): RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(filePath))

    private fun createImageTypeBody(type: String): RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), type)

    fun updateLicense(jobTitleId: Int? = null, about: String? = null, license: String? = null, state: String? = null): Single<LicenseUpdateResponse> =
            profileRemoteRepository.updateLicense(jobTitleId, about, license, state)

    fun requestJobTitle(): Single<JobTitleResponse> = profileRemoteRepository.requestJobTitle()

    fun requestUnreadNotificationCount(): Single<UnReadNotificationCountResponse> =
            profileRemoteRepository.requestUnreadNotificationCount()

    fun requestAffiliationList(): Single<AffiliationResponse> =
            profileRemoteRepository.requestAffiliationList()

    fun saveAffiliations(list: ArrayList<LocationEvent.Affiliation>): Single<BaseResponse> =
            profileRemoteRepository.saveAffiliations(list)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun addWorkExp(request: WorkExpRequest): Single<WorkExpResponse> =
            profileRemoteRepository.addWorkExp(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun requestWorkExpList(request: WorkExpListRequest): Single<WorkExpResponse> =
            profileRemoteRepository.requestWorkExpList(request)

    fun requestSchoolList(): Single<SchoolingResponse> =
            profileRemoteRepository.requestSchoolList()

    fun addSchooling(postMapData: HashMap<Int, PostSchoolData>, list: MutableList<SchoolTypeModel>): Single<BaseResponse> =
            profileRemoteRepository.addSchooling(postMapData, list)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun requestSkillsList(): Single<SkillsResponse> =
            profileRemoteRepository.requestSkillsList()

    fun updateSkills(skills: MutableList<ParentSkillModel>): Single<BaseResponse> =
            profileRemoteRepository.updateSkills(skills)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun uploadCertificateImage(filePath: String, certificateId: String): Single<FileUploadResponse> =
            profileRemoteRepository.uploadCertificateImage(createImageFileBody(filePath), createImageTypeBody(certificateId))

    fun saveCertificate(id: Int, date: String): Single<BaseResponse> =
            profileRemoteRepository.saveCertificate(id, date)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun deleteWorkExperience(id: Int): Single<BaseResponse> =
            profileRemoteRepository.deleteWorkExperience(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }

    fun saveAboutMe(bio: String): Single<BaseResponse> =
            profileRemoteRepository.saveAboutMe(bio)

    fun requestCertificationList(): Single<CertificateResponse> =
            profileRemoteRepository.requestCertificationList()

    fun saveCertificateList(certificateList: ArrayList<CertificatesList>): Single<BaseResponse> =
            profileRemoteRepository.saveCertificateList(certificateList)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }
}