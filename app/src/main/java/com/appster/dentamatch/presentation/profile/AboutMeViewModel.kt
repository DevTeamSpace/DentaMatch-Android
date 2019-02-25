package com.appster.dentamatch.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor

class AboutMeViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableAboutMe = MutableLiveData<BaseResponse>()

    val aboutMe: LiveData<BaseResponse> get() = mutableAboutMe

    fun saveAboutMe(bio: String) =
            addDisposable(
                    profileInteractor.saveAboutMe(bio)
                            .compose(viewModelCompose())
                            .subscribe({}, {})
            )
}