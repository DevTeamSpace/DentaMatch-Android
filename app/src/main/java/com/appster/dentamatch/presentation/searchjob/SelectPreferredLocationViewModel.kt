package com.appster.dentamatch.presentation.searchjob

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import timber.log.Timber
import javax.inject.Inject

class SelectPreferredLocationViewModel
@Inject
constructor(
        private val searchJobInteractor: SearchJobInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "SelectPreferredLocation"
    }

    private val mutablePreferredJobLocationModel = MutableLiveData<PreferredJobLocationModel>()
    private val mutablePreferredJobLocationFailed = MutableLiveData<Throwable>()
    val preferredJobLocationModel: LiveData<PreferredJobLocationModel> get() = mutablePreferredJobLocationModel
    val preferredJobLocationFailed: LiveData<Throwable> get() = mutablePreferredJobLocationFailed

    fun requestPreferredJobLocationList() {
        addDisposable(searchJobInteractor.requestPreferredJobLocationList()
                .compose(viewModelCompose())
                .subscribe({ mutablePreferredJobLocationModel.postValue(it) },
                        {
                            Timber.e(it)
                            mutablePreferredJobLocationFailed.postValue(it)
                        }))
    }
}