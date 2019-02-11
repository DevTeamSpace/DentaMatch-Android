package com.appster.dentamatch.domain.messages

import com.appster.dentamatch.network.response.chat.ChatHistoryResponse
import io.reactivex.Completable
import io.reactivex.Single

class MessagesInteractor(
        private val remoteRepository: MessagesRemoteRepository
) {

    fun requestChatHistory(): Single<ChatHistoryResponse> =
            remoteRepository.requestChatHistory()

    fun deleteUserChat(id: String): Completable =
            remoteRepository.deleteUserChat(id)
                    .ignoreElement()
}