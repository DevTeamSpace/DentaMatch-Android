package com.appster.dentamatch.domain.chat

import com.appster.dentamatch.model.ChatResponse
import io.reactivex.Single

class ChatInteractor(
        private val chatRemoteRepository: ChatRemoteRepository
) {

    fun initChat(recruiterId: String): Single<ChatResponse> =
            chatRemoteRepository.initChat(recruiterId)
}