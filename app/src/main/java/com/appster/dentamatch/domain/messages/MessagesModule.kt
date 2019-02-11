package com.appster.dentamatch.domain.messages

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class MessagesModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): MessagesRetrofit =
            retrofit.create(MessagesRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(messagesRetrofit: MessagesRetrofit,
                                composer: RemoteRepositoryComposer): MessagesRemoteRepository =
            MessagesRemoteRepository(messagesRetrofit, composer)

    @Provides
    @Singleton
    fun provideInteractor(remoteRepository: MessagesRemoteRepository): MessagesInteractor =
            MessagesInteractor(remoteRepository)
}