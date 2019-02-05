package com.appster.dentamatch.presentation.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseViewModel
import com.appster.dentamatch.domain.login.LoginInteractor
import com.appster.dentamatch.domain.login.LoginResponse
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
        private val loginInteractor: LoginInteractor
) : BaseViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val userInfoData = MutableLiveData<LoginResponse>()
    private val mutablePreferredLocationsList = MutableLiveData<PreferredJobLocationModel>()
    val login: LiveData<LoginResponse> get() = userInfoData
    val preferredLocationsList: LiveData<PreferredJobLocationModel> get() = mutablePreferredLocationsList

    fun signIn(email: String, password: String) = addDisposable(
                loginInteractor.signIn(email, password)
                        .compose(viewModelCompose())
                        .subscribe({ userInfoData.postValue(it) }, { Log.e(TAG, "signIn", it) })
    )

    fun signUp(email: String,
               password: String,
               firstName: String,
               lastName: String,
               preferredJobLocationId: Int) = addDisposable(
                loginInteractor.signUp(email, password, firstName, lastName, preferredJobLocationId)
                        .compose(viewModelCompose())
                        .subscribe({ userInfoData.postValue(it) }, { Log.e(TAG, "signUp", it) })
    )


    fun getPreferredJobLocationList() = addDisposable(
                loginInteractor.getPreferred()
                        .compose(viewModelCompose())
                        .subscribe({ mutablePreferredLocationsList.postValue(it) },
                                { Log.e(TAG, "getPreferredJobLocationList", it) })
    )
}