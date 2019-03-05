package com.appster.dentamatch.presentation.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.common.CommonInteractor
import com.appster.dentamatch.domain.messages.MessagesInteractor
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse
import timber.log.Timber

class HomeViewModel(
        private val commonInteractor: CommonInteractor,
        private val profileInteractor: ProfileInteractor,
        private val messagesInteractor: MessagesInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "CommonInteractor"
    }

    private val mutableUnReadNotificationCount = MutableLiveData<UnReadNotificationCountResponse>()
    private val mutableUnreadMessages = MutableLiveData<Int>()

    val unReadNotificationCount: LiveData<UnReadNotificationCountResponse> get() = mutableUnReadNotificationCount
    val unreadMessages: LiveData<Int> get() = mutableUnreadMessages

    fun updateFcmToken(fcmToken: String) =
            addDisposable(
                    commonInteractor.updateFcmToken(fcmToken)
                            .subscribe(
                                    { Timber.d("token updated successfully") },
                                    {
                                        Timber.e(it)
                                        Timber.d("token update fails")
                                    }
                            )
            )

    fun requestUnreadNotificationCount() =
            addDisposable(
                    profileInteractor.requestUnreadNotificationCount()
                            .subscribe({ mutableUnReadNotificationCount.postValue(it) },
                                    { Timber.e(it) })
            )

    fun requestUnreadMessagesCount() =
            addDisposable(
                    messagesInteractor.requestChatHistory()
                            .map { return@map countUnreadMessages(it) }
                            .subscribe({ mutableUnreadMessages.postValue(it) }, { Timber.e(it) })
            )

    private fun countUnreadMessages(it: ChatHistoryResponse): Int {
        var unreadCount = 0
        for (model in it.result.list) {
            unreadCount += model.unreadCount.toInt()
        }
        return unreadCount
    }
}