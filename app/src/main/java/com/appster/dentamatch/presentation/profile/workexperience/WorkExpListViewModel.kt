package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest
import com.appster.dentamatch.network.request.workexp.WorkExpRequest
import com.appster.dentamatch.network.response.workexp.WorkExpResponse
import timber.log.Timber

class WorkExpListViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val  TAG = "WorkExpListViewModel"
    }

    private val mutableWorkExperience = MutableLiveData<WorkExpResponse>()
    private val mutableWorkExperienceList = MutableLiveData<WorkExpResponse>()
    private val mutableFailedWorkExpList = MutableLiveData<Throwable>()

    val workExperience: LiveData<WorkExpResponse> get() = mutableWorkExperience
    val workExpList: LiveData<WorkExpResponse> get() = mutableWorkExperienceList
    val failedWorkExpList: LiveData<Throwable> get() = mutableFailedWorkExpList

    fun addWorkExp(request: WorkExpRequest) =
            addDisposable(
                    profileInteractor.addWorkExp(request)
                            .compose(viewModelCompose())
                            .subscribe({ mutableWorkExperience.postValue(it) }, { Timber.e(it) })
            )

    fun requestWorkExpList(request: WorkExpListRequest) =
            addDisposable(
                    profileInteractor.requestWorkExpList(request)
                            .compose(viewModelCompose())
                            .doOnError { mutableFailedWorkExpList.postValue(it) }
                            .subscribe({ mutableWorkExperienceList.postValue(it) }, { Timber.e(it) })
            )
}