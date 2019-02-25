package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import com.appster.dentamatch.util.Constants
import timber.log.Timber

class UpdateCertificateViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableCertificateUpload = MutableLiveData<FileUploadResponse>()
    private val mutableCertificateError = MutableLiveData<Throwable>()
    private val mutableDentalUpload = MutableLiveData<FileUploadResponse>()
    private val mutableSaveCertificate = MutableLiveData<BaseResponse>()

    val certificateUpload: LiveData<FileUploadResponse> get() = mutableCertificateUpload
    val certificateError: LiveData<Throwable> get() = mutableCertificateError
    val dentalUpload: LiveData<FileUploadResponse> get() = mutableDentalUpload
    val saveCertificate: LiveData<BaseResponse> get() = mutableSaveCertificate

    fun uploadCertificateImage(filePath: String, certificateId: String) =
            addDisposable(
                    profileInteractor.uploadCertificateImage(filePath, certificateId)
                            .compose(viewModelCompose())
                            .doOnError { mutableCertificateError.postValue(it) }
                            .subscribe({ mutableCertificateUpload.postValue(it) }, { Timber.e(it) })
            )

    fun uploadDentalImage(filePath: String) =
            addDisposable(
                    profileInteractor.uploadImage(filePath, Constants.APIS.IMAGE_TYPE_STATE_BOARD)
                            .compose(viewModelCompose())
                            .subscribe({ mutableDentalUpload.postValue(it) }, { Timber.e(it) })
            )

    fun saveCertificate(id: Int, date: String) =
            addDisposable(
                    profileInteractor.saveCertificate(id, date)
                            .compose(viewModelCompose())
                            .subscribe({ mutableSaveCertificate.postValue(it) }, { Timber.e(it) })
            )
}