package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.request.workexp.WorkExpRequest
import com.appster.dentamatch.network.response.workexp.WorkExpResponse
import timber.log.Timber

class ViewAndEditWorkExperienceViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableDeleteWorkExperience = MutableLiveData<BaseResponse>()
    private val mutableAddWorkExperience = MutableLiveData<WorkExpResponse>()

    val deleteWorkExperience: LiveData<BaseResponse> get() = mutableDeleteWorkExperience
    val addWorkExperience: LiveData<WorkExpResponse> get() = mutableAddWorkExperience

    fun deleteWorkExperience(id: Int) =
            addDisposable(
                    profileInteractor.deleteWorkExperience(id)
                            .compose(viewModelCompose())
                            .subscribe({ mutableDeleteWorkExperience.postValue(it) }, { Timber.e(it) })
            )

    fun addWorkExp(workExpRequest: WorkExpRequest) =
            addDisposable(
                    profileInteractor.addWorkExp(workExpRequest)
                            .compose(viewModelCompose())
                            .subscribe({ mutableAddWorkExperience.postValue(it) }, { Timber.e(it) })
            )
}