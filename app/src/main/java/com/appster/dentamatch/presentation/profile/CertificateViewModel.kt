package com.appster.dentamatch.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.certificates.CertificateResponse
import com.appster.dentamatch.network.response.certificates.CertificatesList
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse
import timber.log.Timber
import java.util.ArrayList

class CertificateViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableCertificationList = MutableLiveData<CertificateResponse>()
    private val mutableCertificationListFailed = MutableLiveData<Throwable>()
    private val mutableCertificateUpload = MutableLiveData<FileUploadResponse>()
    private val mutableCertificateListSave = MutableLiveData<BaseResponse>()

    val certificationList: LiveData<CertificateResponse> get() = mutableCertificationList
    val certificationListFailed: LiveData<Throwable> get() = mutableCertificationListFailed
    val certificateUpload: LiveData<FileUploadResponse> get() = mutableCertificateUpload
    val certificateListSave: LiveData<BaseResponse> get() = mutableCertificateListSave

    fun requestCertificationList() =
            addDisposable(
                    profileInteractor.requestCertificationList()
                            .compose(viewModelCompose())
                            .doOnError { mutableCertificationListFailed.postValue(it) }
                            .subscribe({ mutableCertificationList.postValue(it) }, { Timber.e(it) })
            )

    fun uploadCertificateImage(filePath: String, certificateId: String) =
            addDisposable(
                    profileInteractor.uploadCertificateImage(filePath, certificateId)
                            .compose(viewModelCompose())
                            .subscribe({ mutableCertificateUpload.postValue(it) }, { Timber.e(it) })
            )

    fun saveCertificateList(certificateList: ArrayList<CertificatesList>) =
            addDisposable(
                    profileInteractor.saveCertificateList(certificateList)
                            .compose(viewModelCompose())
                            .subscribe({ mutableCertificateListSave.postValue(it) }, { Timber.e(it) })
            )
}