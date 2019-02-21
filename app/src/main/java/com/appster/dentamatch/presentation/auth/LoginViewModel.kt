package com.appster.dentamatch.presentation.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import com.appster.dentamatch.domain.auth.AuthResponse
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import timber.log.Timber

class LoginViewModel(
        private val authInteractor: AuthInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val userInfoData = MutableLiveData<AuthResponse>()
    private val mutablePreferredLocationsList = MutableLiveData<PreferredJobLocationModel>()
    val login: LiveData<AuthResponse> get() = userInfoData
    val preferredLocationsList: LiveData<PreferredJobLocationModel> get() = mutablePreferredLocationsList

    fun signIn(email: String, password: String) = addDisposable(
                authInteractor.signIn(email, password)
                        .compose(viewModelCompose())
                        .subscribe({ userInfoData.postValue(it) }, { Timber.e(it) })
    )

    fun signUp(email: String,
               password: String,
               firstName: String,
               lastName: String,
               preferredJobLocationId: Int) = addDisposable(
                authInteractor.signUp(email, password, firstName, lastName, preferredJobLocationId)
                        .compose(viewModelCompose())
                        .subscribe({ userInfoData.postValue(it) }, { Timber.e(it) })
    )


    fun getPreferredJobLocationList() = addDisposable(
                authInteractor.getPreferred()
                        .compose(viewModelCompose())
                        .subscribe({ mutablePreferredLocationsList.postValue(it) },
                                { Timber.e(it) })
    )
}