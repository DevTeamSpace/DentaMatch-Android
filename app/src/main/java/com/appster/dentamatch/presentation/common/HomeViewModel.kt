package com.appster.dentamatch.presentation.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.common.CommonInteractor
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse

class HomeViewModel(
        private val commonInteractor: CommonInteractor,
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "CommonInteractor"
    }

    private val mutableUnReadNotificationCount = MutableLiveData<UnReadNotificationCountResponse>()

    val unReadNotificationCount: LiveData<UnReadNotificationCountResponse> get() = mutableUnReadNotificationCount

    fun updateFcmToken(fcmToken: String) =
            addDisposable(
                    commonInteractor.updateFcmToken(fcmToken)
                            .subscribe(
                                    { Log.d(TAG, "token updated successfully") },
                                    {
                                        Log.e(TAG, "updateFcmToken", it)
                                        Log.d(TAG, "token update fails")
                                    }
                            )
            )

    fun requestUnreadNotificationCount() =
            addDisposable(
                    profileInteractor.requestUnreadNotificationCount()
                            .subscribe({ mutableUnReadNotificationCount.postValue(it) },
                                    { Log.e(TAG, "requestUnreadNotificationCount", it) })
            )
}