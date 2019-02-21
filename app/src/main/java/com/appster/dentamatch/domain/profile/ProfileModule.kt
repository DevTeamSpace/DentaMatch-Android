package com.appster.dentamatch.domain.profile

import android.content.Context
import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ProfileModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): ProfileRetrofit = retrofit.create(ProfileRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(composer: RemoteRepositoryComposer,
                                profileRetrofit: ProfileRetrofit) =
            ProfileRemoteRepository(composer, profileRetrofit)

    @Provides
    fun provideInteractor(remoteRepository: ProfileRemoteRepository, context: Context) =
            ProfileInteractor(remoteRepository, context)
}