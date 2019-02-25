package com.appster.dentamatch.presentation.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import timber.log.Timber

class ForgotPasswordViewModel(
        private val authInteractor: AuthInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "ForgotPasswordViewModel"
    }

    private val mutableForgotPassword = MutableLiveData<Boolean>()

    val forgotPassword: LiveData<Boolean> get() = mutableForgotPassword

    fun forgotPassword(eMail: String) =
            addDisposable(
                    authInteractor.forgotPassword(eMail)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableForgotPassword.postValue(true) },
                                    { Timber.e(it)})
            )
}