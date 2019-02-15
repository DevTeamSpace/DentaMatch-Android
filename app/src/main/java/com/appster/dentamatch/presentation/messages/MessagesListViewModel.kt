package com.appster.dentamatch.presentation.messages

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.messages.MessagesInteractor
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse
import timber.log.Timber

class MessagesListViewModel(
        private val messagesInteractor: MessagesInteractor
) : BaseLoadingViewModel() {

    private val mutableChatHistory = MutableLiveData<ChatHistoryResponse>()
    private val mutableChatDelete = MutableLiveData<Pair<String, Int>>()

    val chatHistory: LiveData<ChatHistoryResponse> get() = mutableChatHistory
    val chatDelete: LiveData<Pair<String, Int>> get() = mutableChatDelete

    fun requestChatHistory() =
            addDisposable(
                messagesInteractor.requestChatHistory()
                        .compose(viewModelCompose())
                        .subscribe({ mutableChatHistory.postValue(it) }, { Timber.e(it) })
            )

    fun deleteUserChat(id: String, position: Int) =
            addDisposable(
                    messagesInteractor.deleteUserChat(id)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableChatDelete.postValue(Pair(id, position)) }, { Timber.e(it) })
            )
}