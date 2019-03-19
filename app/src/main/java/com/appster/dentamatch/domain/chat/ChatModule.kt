package com.appster.dentamatch.domain.chat

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ChatModule {

    @Provides
    fun provideChatRetrofit(retrofit: Retrofit): ChatRetrofit = retrofit.create(ChatRetrofit::class.java)

    @Provides
    fun provideChatRemoteRepository(chatRetrofit: ChatRetrofit,
                                    composer: RemoteRepositoryComposer): ChatRemoteRepository =
            ChatRemoteRepository(chatRetrofit, composer)

    @Provides
    fun provideChatInteractor(chatRemoteRepository: ChatRemoteRepository): ChatInteractor =
            ChatInteractor(chatRemoteRepository)
}