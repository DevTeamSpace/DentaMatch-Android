package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.profile.LicenseUpdateResponse
import timber.log.Timber

class UpdateLicenseViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableLicenseUpdate = MutableLiveData<LicenseUpdateResponse>()

    val licenseUpdate: LiveData<LicenseUpdateResponse> get() = mutableLicenseUpdate

    fun updateLicence(license: String, state: String) =
            addDisposable(
                    profileInteractor.updateLicense(license = license, state = state)
                            .compose(viewModelCompose())
                            .subscribe({ mutableLicenseUpdate.postValue(it) }, { Timber.e(it) })
            )
}