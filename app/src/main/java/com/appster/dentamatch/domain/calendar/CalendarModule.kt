package com.appster.dentamatch.domain.calendar

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CalendarModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): CalendarRetrofit =
            retrofit.create(CalendarRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(retrofit: CalendarRetrofit,
                                composer: RemoteRepositoryComposer): CalendarRemoteRepository =
            CalendarRemoteRepository(retrofit, composer)

    @Provides
    @Singleton
    fun provideInteractor(remoteRepository: CalendarRemoteRepository,
                          context: Context): CalendarInteractor =
            CalendarInteractor(context, remoteRepository)
}