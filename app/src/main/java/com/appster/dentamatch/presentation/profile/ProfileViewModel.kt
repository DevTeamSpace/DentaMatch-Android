package com.appster.dentamatch.presentation.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import com.appster.dentamatch.network.response.profile.ProfileResponse
import timber.log.Timber

class ProfileViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "ProfileViewModel"
    }

    private val mutableProfile = MutableLiveData<ProfileResponse>()
    private val mutableUnreadNotificationCount = MutableLiveData<UnReadNotificationCountResponse>()

    val profile: LiveData<ProfileResponse> get() = mutableProfile
    val unreadNotificationCount: LiveData<UnReadNotificationCountResponse> get() = mutableUnreadNotificationCount

    fun requestProfile() =
            addDisposable(
                  profileInteractor.requestProfile()
                          .compose(viewModelCompose())
                          .subscribe({ mutableProfile.postValue(it) },
                                  { Timber.e(it) })
            )

    fun requestUnreadNotificationCount() =
            addDisposable(
                    profileInteractor.requestUnreadNotificationCount()
                            .compose(viewModelCompose())
                            .subscribe({ mutableUnreadNotificationCount.postValue(it) },
                                    {Timber.e(it)})
            )
}