package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.request.workexp.WorkExpRequest
import com.appster.dentamatch.network.response.workexp.WorkExpResponse
import timber.log.Timber

class WorkExperienceDetailViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableWorkExpResponse = MutableLiveData<WorkExpResponse>()

    val workExpResponse: LiveData<WorkExpResponse> get() = mutableWorkExpResponse

    fun addWorkExp(request: WorkExpRequest) =
            addDisposable(
                    profileInteractor.addWorkExp(request)
                            .compose(viewModelCompose())
                            .subscribe({ mutableWorkExpResponse.postValue(it) }, { Timber.e(it) })
            )
}