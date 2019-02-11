package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class SearchJobModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): SearchJobRetrofit = retrofit.create(SearchJobRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(composer: RemoteRepositoryComposer, searchJobRetrofit: SearchJobRetrofit) =
            SearchJobRemoteRepository(composer, searchJobRetrofit)

    @Provides
    @Singleton
    fun profideInteractor(remoteRepository: SearchJobRemoteRepository) =
            SearchJobInteractor(remoteRepository)
}