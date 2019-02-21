package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.model.ParentSkillModel
import com.appster.dentamatch.network.response.skills.SkillsResponse
import timber.log.Timber

class SkillsViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableSkills = MutableLiveData<SkillsResponse>()
    private val mutableSkillsError = MutableLiveData<Throwable>()
    private val mutableSkillsUpdate = MutableLiveData<BaseResponse>()

    val skills: LiveData<SkillsResponse> get() = mutableSkills
    val skillsError: LiveData<Throwable> get() = mutableSkillsError
    val skillsUpdate: LiveData<BaseResponse> get() = mutableSkillsUpdate

    fun requestSkillsList() =
            addDisposable(
                    profileInteractor.requestSkillsList()
                            .compose(viewModelCompose())
                            .doOnError { mutableSkillsError.postValue(it) }
                            .subscribe({ mutableSkills.postValue(it) }, { Timber.e(it) })
            )

    fun updateSkills(skills: MutableList<ParentSkillModel>) =
            addDisposable(
                    profileInteractor.updateSkills(skills)
                            .compose(viewModelCompose())
                            .subscribe({ mutableSkillsUpdate.postValue(it) }, { Timber.e(it) })
            )
}