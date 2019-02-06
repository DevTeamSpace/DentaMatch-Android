package com.appster.dentamatch.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse

class CreateProfileViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "CreateProfileViewModel"
    }

    private val mutableLicenseUpdate = MutableLiveData<LicenseUpdateResponse>()
    private val mutableJobTitle = MutableLiveData<JobTitleResponse>()
    private val mutableUploadImage = MutableLiveData<FileUploadResponse>()

    val licenseUpdate: LiveData<LicenseUpdateResponse> get() = mutableLicenseUpdate
    val jobTitle: LiveData<JobTitleResponse> get() = mutableJobTitle
    val uploadImage: LiveData<FileUploadResponse> get() = mutableUploadImage

    fun updateLicence(jobTitleId: Int, about: String, license: String?, state: String?) =
            addDisposable(
                    profileInteractor.updateLicense(jobTitleId, about, license, state)
                            .compose(viewModelCompose())
                            .subscribe({ mutableLicenseUpdate.postValue(it) },
                                    { Log.e(TAG, "updateLicence", it) })
            )

    fun uploadImage(filePath: String) =
            addDisposable(
                    profileInteractor.uploadImage(filePath)
                            .compose(viewModelCompose())
                            .subscribe({ mutableUploadImage.postValue(it) },
                                    { Log.e(TAG, "uploadImage", it) })
            )

    fun requestJobTitle() =
            addDisposable(
                    profileInteractor.requestJobTitle()
                            .compose(viewModelCompose())
                            .subscribe({ mutableJobTitle.postValue(it) },
                                    { Log.e(TAG, "requestJobTitle", it) })
            )
}