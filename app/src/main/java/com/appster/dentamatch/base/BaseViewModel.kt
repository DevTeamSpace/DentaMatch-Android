package com.appster.dentamatch.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    private val mutableLoading = MutableLiveData<Boolean>()
    private val mutableError = MutableLiveData<Throwable>()

    val loading: LiveData<Boolean> get() = mutableLoading
    val error: LiveData<Throwable> get() = mutableError

    protected fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun <T> viewModelCompose(): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnSubscribe { mutableLoading.postValue(true) }
                        .doOnError { throwable -> mutableError.postValue(throwable) }
                        .doFinally { mutableLoading.postValue(false) }
            }

    @CallSuper
    override fun onCleared() = disposables.clear()
}