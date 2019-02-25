package com.appster.dentamatch.presentation.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import timber.log.Timber

class ResetPasswordViewModel(
        private val authInteractor: AuthInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "ResetPasswordViewModel"
    }

    private val mutableChangePassword = MutableLiveData<Boolean>()

    val changePassword: LiveData<Boolean> get() = mutableChangePassword

    fun changePassword(confirmPassword: String, oldPassword: String, newPassword: String) =
            addDisposable(
                    authInteractor.changePassword(confirmPassword, oldPassword, newPassword)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableChangePassword.postValue(true) },
                                    { Timber.e(it)})
            )
}