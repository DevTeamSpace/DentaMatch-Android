package com.appster.dentamatch.domain.profile

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ProfileModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): ProfileRetrofit = retrofit.create(ProfileRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(composer: RemoteRepositoryComposer,
                                profileRetrofit: ProfileRetrofit) =
            ProfileRemoteRepository(composer, profileRetrofit)

    @Provides
    @Singleton
    fun provideInteractor(remoteRepository: ProfileRemoteRepository) = ProfileInteractor(remoteRepository)
}