package com.appster.dentamatch.domain.chat

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.model.ChatResponse
import io.reactivex.Single

class ChatRemoteRepository(
        private val chatRetrofit: ChatRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun initChat(recruiterId: String): Single<ChatResponse> =
            chatRetrofit.initChat(recruiterId)
                    .compose(composer.apiCompose())
}