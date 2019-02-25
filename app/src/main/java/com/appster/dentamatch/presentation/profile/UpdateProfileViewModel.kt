package com.appster.dentamatch.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.auth.AuthInteractor
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import com.appster.dentamatch.util.Constants
import timber.log.Timber
import javax.inject.Inject

class UpdateProfileViewModel
@Inject
constructor(
        private val profileInteractor: ProfileInteractor,
        private val authInteractor: AuthInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "UpdateProfileViewModel"
    }

    private val mutableProfileResponse = MutableLiveData<ProfileResponse>()
    private val mutablePreferredJobLocation = MutableLiveData<PreferredJobLocationModel>()
    private val mutableUpdateUserProfile = MutableLiveData<BaseResponse>()
    private val mutableUploadImage = MutableLiveData<FileUploadResponse>()

    val profileResponse: LiveData<ProfileResponse> get() = mutableProfileResponse
    val preferredJobLocation: LiveData<PreferredJobLocationModel> get() = mutablePreferredJobLocation
    val updateUserProfile: LiveData<BaseResponse> get() = mutableUpdateUserProfile
    val uploadImage: LiveData<FileUploadResponse> get() = mutableUploadImage

    fun requestProfile() =
            addDisposable(
                    profileInteractor.requestProfile()
                            .compose(viewModelCompose())
                            .subscribe({ mutableProfileResponse.postValue(it) },
                                    { Timber.e(it) })
            )

    fun requestPreferredJobLocation() =
            addDisposable(
                    authInteractor.getPreferred()
                            .compose(viewModelCompose())
                            .subscribe({ mutablePreferredJobLocation.postValue(it) },
                                    { Timber.e(it) })
            )

    fun updateUserProfile(firstName: String,
                          lastName: String,
                          about: String,
                          jobTitleId: Int,
                          preferredJobLocationId: String,
                          state: String?,
                          license: String?) =
            addDisposable(
                    profileInteractor.updateUserProfile(firstName,
                            lastName,
                            about,
                            jobTitleId,
                            preferredJobLocationId,
                            state,
                            license)
                            .compose(viewModelCompose())
                            .subscribe({ mutableUpdateUserProfile.postValue(it) },
                                    { Timber.e(it) })
            )

    fun uploadImage(filePath: String) =
            addDisposable(
                    profileInteractor.uploadImage(filePath, Constants.APIS.IMAGE_TYPE_PIC)
                            .compose(viewModelCompose())
                            .subscribe({ mutableUploadImage.postValue(it) },
                                    { Timber.e(it) })
            )
}