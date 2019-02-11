package com.appster.dentamatch.presentation.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.common.CommonInteractor
import com.appster.dentamatch.network.response.profile.StateResponse
import timber.log.Timber

class SearchStateViewModel(
        private val commonInteractor: CommonInteractor
) : BaseLoadingViewModel() {

    private val mutableStateList = MutableLiveData<StateResponse>()

    val stateList: LiveData<StateResponse> get() = mutableStateList

    fun requestStateList() =
            addDisposable(
                    commonInteractor.requestStateList()
                            .compose(viewModelCompose())
                            .subscribe({ mutableStateList.postValue(it) }, { Timber.e(it) })
            )
}