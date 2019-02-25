package com.appster.dentamatch.domain.calendar

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CalendarModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): CalendarRetrofit =
            retrofit.create(CalendarRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(retrofit: CalendarRetrofit,
                                composer: RemoteRepositoryComposer): CalendarRemoteRepository =
            CalendarRemoteRepository(retrofit, composer)

    @Provides
    fun provideInteractor(remoteRepository: CalendarRemoteRepository,
                          context: Context): CalendarInteractor =
            CalendarInteractor(context, remoteRepository)
}