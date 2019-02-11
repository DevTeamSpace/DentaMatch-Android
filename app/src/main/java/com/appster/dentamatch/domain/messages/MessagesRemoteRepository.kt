package com.appster.dentamatch.domain.messages

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.chat.Recruiter
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse
import io.reactivex.Single

class MessagesRemoteRepository(
        private val messagesRetrofit: MessagesRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun requestChatHistory(): Single<ChatHistoryResponse> =
            messagesRetrofit.requestChatHistory()
                    .compose(composer.apiCompose())

    fun deleteUserChat(id: String): Single<BaseResponse> =
            messagesRetrofit.deleteUserChat(Recruiter(id))
                    .compose(composer.apiCompose())
}