package com.appster.dentamatch.domain.chat

import com.appster.dentamatch.model.ChatResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChatRetrofit {

    @FormUrlEncoded
    @POST("chat/init-chat")
    fun initChat(@Field("recruiterId") recruiterId: String): Single<ChatResponse>
}