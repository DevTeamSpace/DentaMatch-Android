package com.appster.dentamatch.domain.profile

import android.text.TextUtils
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.eventbus.LocationEvent
import com.appster.dentamatch.model.ParentSkillModel
import com.appster.dentamatch.model.SchoolTypeModel
import com.appster.dentamatch.network.request.affiliation.AffiliationPostRequest
import com.appster.dentamatch.network.request.affiliation.OtherAffiliationRequest
import com.appster.dentamatch.network.request.auth.LicenseRequest
import com.appster.dentamatch.network.request.certificates.CertificateRequest
import com.appster.dentamatch.network.request.certificates.UpdateCertificates
import com.appster.dentamatch.network.request.profile.AboutMeRequest
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest
import com.appster.dentamatch.network.request.schools.AddSchoolRequest
import com.appster.dentamatch.network.request.schools.PostSchoolData
import com.appster.dentamatch.network.request.skills.SkillsUpdateRequest
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
import com.appster.dentamatch.util.PreferenceUtil
import com.appster.dentamatch.util.Utils
import io.reactivex.Single
import okhttp3.RequestBody
import java.util.ArrayList
import java.util.HashMap

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

    fun updateLicense(jobTitleId: Int?, about: String?, license: String?, state: String?): Single<LicenseUpdateResponse> =
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

    private fun createUpdateLicenseRequest(jobTitleId: Int?, about: String?, license: String?, state: String?): LicenseRequest =
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

    fun requestAffiliationList(): Single<AffiliationResponse> =
            profileRetrofit.requestAffiliationList()
                    .compose(composer.apiCompose())

    fun saveAffiliations(list: ArrayList<LocationEvent.Affiliation>): Single<BaseResponse> =
            profileRetrofit.saveAffiliations(createSaveAffiliationsRequest(list))
                    .compose(composer.apiCompose())

    private fun createSaveAffiliationsRequest(affiliations: ArrayList<LocationEvent.Affiliation>): AffiliationPostRequest =
            AffiliationPostRequest().apply {
                val idList = ArrayList<Int>()
                val otherList = ArrayList<OtherAffiliationRequest>()
                for (i in affiliations.indices) {
                    val affiliation = affiliations[i]
                    if (!affiliation.affiliationName.equals(Constants.OTHERS, ignoreCase = true)
                            && affiliation.jobSeekerAffiliationStatus == 1) {
                        idList.add(affiliation.affiliationId)
                    } else {
                        if (affiliation.jobSeekerAffiliationStatus == 1) {
                            val otherAffiliationRequest = OtherAffiliationRequest()
                            otherAffiliationRequest.setAffiliationId(affiliation.affiliationId)
                            otherAffiliationRequest.setOtherAffiliation(affiliation.otherAffiliation)
                            otherList.add(otherAffiliationRequest)
                        }
                    }
                }

                setIdList(idList)
                setOtherAffiliationList(otherList)
            }

    fun addWorkExp(request: WorkExpRequest): Single<WorkExpResponse> =
            profileRetrofit.addWorkExp(request)
                    .compose(composer.apiCompose())

    fun requestWorkExpList(request: WorkExpListRequest): Single<WorkExpResponse> =
            profileRetrofit.requestWorkExpList(request)
                    .compose(composer.apiCompose())

    fun requestSchoolList(): Single<SchoolingResponse> =
            profileRetrofit.requestSchoolList()
                    .compose(composer.apiCompose())

    fun addSchooling(postMapData: HashMap<Int, PostSchoolData>, list: MutableList<SchoolTypeModel>): Single<BaseResponse> =
            profileRetrofit.addSchooling(createAddSchoolingRequest(postMapData, list))
                    .compose(composer.apiCompose())

    private fun createAddSchoolingRequest(hashMap: HashMap<Int, PostSchoolData>, list: MutableList<SchoolTypeModel>) =
            AddSchoolRequest().apply {
                schoolingData = ArrayList<PostSchoolData>().apply {
                    for (entry in hashMap.entries) {
                        val school = PostSchoolData()
                        var isMatchSchoolName = false
                        for (i in 0 until list.size) {
                            for (j in 0 until list[i].schoolList.size) {
                                val schoolModel = list[i].schoolList[j]
                                if (entry.value.schoolName.equals(schoolModel.schoolName, true)) {
                                    isMatchSchoolName = true
                                    school.schoolName = entry.value.schoolName.trim { it <= ' ' }
                                    school.schoolId = schoolModel.schoolId
                                    school.otherSchooling = ""
                                    break
                                }
                            }
                        }
                        if (!isMatchSchoolName) {
                            school.schoolId = Integer.parseInt(entry.value.otherId)
                            school.otherSchooling = entry.value.schoolName.trim { it <= ' ' }
                            school.schoolName = ""
                        }
                        school.yearOfGraduation = entry.value.yearOfGraduation
                        add(school)
                    }
                }
            }

    fun requestSkillsList(): Single<SkillsResponse> =
            profileRetrofit.requestSkillsList()
                    .compose(composer.apiCompose())

    fun updateSkills(skills: MutableList<ParentSkillModel>): Single<BaseResponse> =
            profileRetrofit.updateSkills(createSkillsRequest(skills))
                    .compose(composer.apiCompose())

    private fun createSkillsRequest(skills: MutableList<ParentSkillModel>) =
            SkillsUpdateRequest().apply {
                val skillsList = ArrayList<Int>()
                val othersList = ArrayList<UpdateCertificates>()
                for (parentSkillModel in skills) {
                    if (parentSkillModel.skillName.equals(Constants.OTHERS, ignoreCase = true)) {
                        val otherSkill = parentSkillModel.otherSkill
                        if (!TextUtils.isEmpty(otherSkill)) {
                            val obj = UpdateCertificates()
                            obj.id = parentSkillModel.id
                            obj.value = otherSkill.trim { it <= ' ' }
                            othersList.add(obj)
                        }
                    }
                    for (subSkillModel in parentSkillModel.subSkills) {
                        if (subSkillModel.isSelected == 1) {
                            if (!subSkillModel.skillName.equals(Constants.OTHERS, ignoreCase = true)) {
                                skillsList.add(subSkillModel.id)
                            } else {
                                val obj = UpdateCertificates()
                                obj.id = subSkillModel.id
                                val otherText = subSkillModel.otherText
                                if (!TextUtils.isEmpty(otherText)) {
                                    obj.value = otherText.trim { it <= ' ' }
                                }
                                othersList.add(obj)
                            }
                        }
                    }
                    setSkills(skillsList)
                    setOther(othersList)
                }
            }

    fun uploadCertificateImage(fileBody: RequestBody, typeBody: RequestBody): Single<FileUploadResponse> =
            profileRetrofit.uploadCertificateImage(fileBody, typeBody)
                    .compose(composer.apiCompose())

    fun saveCertificate(id: Int, date: String): Single<BaseResponse> =
            profileRetrofit.saveCertificate(createSaveCertificateRequest(id, date))
                    .compose(composer.apiCompose())

    private fun createSaveCertificateRequest(id: Int, date: String) =
            CertificateRequest().apply {
                val certificateRequest = CertificateRequest()
                val updateCertificatesArrayList = ArrayList<UpdateCertificates>()
                val updateCertificates = UpdateCertificates()
                updateCertificates.id = id
                updateCertificates.value = date
                updateCertificatesArrayList.add(updateCertificates)
                certificateRequest.setUpdateCertificatesList(updateCertificatesArrayList)
                return certificateRequest
            }

    fun deleteWorkExperience(id: Int): Single<BaseResponse> =
            profileRetrofit.deleteWorkExperience(id)
                    .compose(composer.apiCompose())

    fun saveAboutMe(bio: String): Single<BaseResponse> =
            profileRetrofit.saveAboutMe(AboutMeRequest().apply { setAboutMe(bio) })
                    .compose(composer.apiCompose())

    fun requestCertificationList(): Single<CertificateResponse> =
            profileRetrofit.requestCertificationList()
                    .compose(composer.apiCompose())

    fun saveCertificateList(certificateList: ArrayList<CertificatesList>): Single<BaseResponse> =
            profileRetrofit.saveCertificateList(createSaveCertificateListRequest(certificateList))
                    .compose(composer.apiCompose())

    private fun createSaveCertificateListRequest(certificateList: ArrayList<CertificatesList>) =
            CertificateRequest().apply {
                this.setUpdateCertificatesList(
                        ArrayList<UpdateCertificates>().apply {
                            for (i in certificateList.indices) {
                                if (certificateList[i].isImageUploaded
                                        && !TextUtils.isEmpty(certificateList[i].image)) {
                                    val updateCertificates = UpdateCertificates()
                                    updateCertificates.id = certificateList[i].id
                                    updateCertificates.value = certificateList[i].validityDate
                                    this.add(updateCertificates)
                                }
                            }
                        }
                )
            }
}