package com.appster.dentamatch.domain.messages

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MessagesModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): MessagesRetrofit =
            retrofit.create(MessagesRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(messagesRetrofit: MessagesRetrofit,
                                composer: RemoteRepositoryComposer): MessagesRemoteRepository =
            MessagesRemoteRepository(messagesRetrofit, composer)

    @Provides
    fun provideInteractor(remoteRepository: MessagesRemoteRepository): MessagesInteractor =
            MessagesInteractor(remoteRepository)
}