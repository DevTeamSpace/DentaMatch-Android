package com.appster.dentamatch.domain.messages

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.network.request.chat.Recruiter
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MessagesRetrofit {

    @GET("users/chat-user-list")
    fun requestChatHistory(): Single<ChatHistoryResponse>

    @POST("chat/delete")
    fun deleteUserChat(@Body recruiter: Recruiter): Single<BaseResponse>
}