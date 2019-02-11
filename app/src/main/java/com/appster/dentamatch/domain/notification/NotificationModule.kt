package com.appster.dentamatch.domain.notification

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NotificationModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): NotificationRetrofit =
            retrofit.create(NotificationRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(notificationRetrofit: NotificationRetrofit,
                                composer: RemoteRepositoryComposer): NotificationRemoteRepository =
            NotificationRemoteRepository(notificationRetrofit, composer)

    @Provides
    @Singleton
    fun provideInteractor(remoteRepository: NotificationRemoteRepository): NotificationInteractor =
            NotificationInteractor(remoteRepository)
}