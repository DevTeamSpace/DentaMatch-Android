package com.appster.dentamatch.domain.notification

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NotificationModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): NotificationRetrofit =
            retrofit.create(NotificationRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(notificationRetrofit: NotificationRetrofit,
                                composer: RemoteRepositoryComposer): NotificationRemoteRepository =
            NotificationRemoteRepository(notificationRetrofit, composer)

    @Provides
    fun provideInteractor(remoteRepository: NotificationRemoteRepository,
                          context: Context): NotificationInteractor =
            NotificationInteractor(remoteRepository, context)
}