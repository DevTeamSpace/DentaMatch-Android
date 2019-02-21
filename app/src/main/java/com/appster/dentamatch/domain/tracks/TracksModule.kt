package com.appster.dentamatch.domain.tracks

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TracksModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): TracksRetrofit = retrofit.create(TracksRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(tracksRetrofit: TracksRetrofit,
                                composer: RemoteRepositoryComposer): TracksRemoteRepository =
            TracksRemoteRepository(tracksRetrofit, composer)

    @Provides
    fun provideInteractor(tracksRemoteRepository: TracksRemoteRepository,
                          context: Context): TracksInteractor =
            TracksInteractor(tracksRemoteRepository, context)
}