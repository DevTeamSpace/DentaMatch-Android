package com.appster.dentamatch.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.CompletableTransformer
import io.reactivex.SingleTransformer

open class BaseLoadingViewModel : BaseViewModel() {

    protected val mutableLoading = MutableLiveData<Boolean>()
    protected val mutableError = MutableLiveData<Throwable>()

    val loading: LiveData<Boolean> get() = mutableLoading
    val error: LiveData<Throwable> get() = mutableError

    protected fun <T> viewModelCompose(): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnSubscribe { mutableLoading.postValue(true) }
                        .doOnError { throwable -> mutableError.postValue(throwable) }
                        .doFinally { mutableLoading.postValue(false) }
            }

    protected fun viewModelCompletableCompose() =
            CompletableTransformer {
                it.doOnSubscribe { mutableLoading.postValue(true) }
                        .doOnError { throwable -> mutableError.postValue(throwable) }
                        .doFinally { mutableLoading.postValue(false) }
            }
}