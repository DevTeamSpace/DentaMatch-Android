package com.appster.dentamatch.domain.searchjob

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SearchJobModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): SearchJobRetrofit = retrofit.create(SearchJobRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(composer: RemoteRepositoryComposer, searchJobRetrofit: SearchJobRetrofit) =
            SearchJobRemoteRepository(composer, searchJobRetrofit)

    @Provides
    fun provideInteractor(remoteRepository: SearchJobRemoteRepository,
                          context: Context) =
            SearchJobInteractor(remoteRepository, context)
}