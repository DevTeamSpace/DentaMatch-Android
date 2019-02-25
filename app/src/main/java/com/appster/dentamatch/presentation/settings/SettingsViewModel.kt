package com.appster.dentamatch.presentation.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.settings.SettingsInteractor
import com.appster.dentamatch.model.UserModel
import timber.log.Timber

class SettingsViewModel(
        private val settingsInteractor: SettingsInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "SettingsViewModel"
    }

    private val mutableUserModel = MutableLiveData<UserModel>()
    private val mutableLogout = MutableLiveData<Boolean>()
    val userModel: LiveData<UserModel> get() = mutableUserModel
    val logout: LiveData<Boolean> get() = mutableLogout

    fun updateLocation(lat: String?,
                       lng: String?,
                       zipCode: String,
                       address: String?,
                       country: String?,
                       state: String?,
                       city: String?) {
        val userModel = createUserModel(lat, lng, zipCode, address, country, state, city)
        addDisposable(
                settingsInteractor.updateUserLocation(userModel)
                        .compose(viewModelCompletableCompose())
                        .subscribe({ mutableUserModel.postValue(userModel) },
                                { Timber.e(it) })
        )
    }

    fun logout() = addDisposable(
            settingsInteractor.logout()
                    .compose(viewModelCompletableCompose())
                    .subscribe({ mutableLogout.postValue(true) }, { Timber.e(it) })
    )

    private fun createUserModel(lat: String?,
                                lng: String?,
                                zipCode: String,
                                address: String?,
                                country: String?,
                                state: String?,
                                city: String?) =
            UserModel().apply {
                latitude = lat
                longitude = lng
                postalCode = zipCode
                preferredJobLocation = address
                preferredCountry = country
                preferredState = state
                preferredCity = city
            }
}