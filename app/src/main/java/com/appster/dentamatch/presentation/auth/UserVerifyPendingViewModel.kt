package com.appster.dentamatch.presentation.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import com.appster.dentamatch.network.response.auth.UserVerifiedStatus
import javax.inject.Inject

class UserVerifyPendingViewModel
@Inject
constructor(
            private val authInteractor: AuthInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "UserVerifyPendingViewMo"
    }

    private val mutableUserVerified = MutableLiveData<UserVerifiedStatus>()
    val userVerified: LiveData<UserVerifiedStatus> get() = mutableUserVerified

    fun checkUserVerified() {
        addDisposable(authInteractor.checkUserVerified()
                .compose(viewModelCompose())
                .subscribe({ mutableUserVerified.postValue(it) }, { Log.e(TAG, "checkUserVerified", it) }))
    }
}