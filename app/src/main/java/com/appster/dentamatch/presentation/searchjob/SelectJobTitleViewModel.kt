package com.appster.dentamatch.presentation.searchjob

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.model.JobTitleListModel
import com.appster.dentamatch.util.PreferenceUtil
import timber.log.Timber
import java.util.ArrayList

class SelectJobTitleViewModel(
        private val searchJobInteractor: SearchJobInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "SelectJobTitleViewModel"
    }

    private val mutableJobsList = MutableLiveData<ArrayList<JobTitleListModel>>()
    val jobsList: LiveData<ArrayList<JobTitleListModel>> get() = mutableJobsList

    fun requestJobsList() =
        addDisposable(searchJobInteractor.requestJobTitles()
                .compose(viewModelCompose())
                .subscribe(
                        {
                            mutableJobsList.postValue(it)
                            PreferenceUtil.setSearchJobTitleList(it)
                        },
                        { Timber.e(it)}))
}