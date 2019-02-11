package com.appster.dentamatch.domain.auth

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideLoginRetrofit(retrofit: Retrofit): AuthRetrofit = retrofit.create(AuthRetrofit::class.java)

    @Provides
    @Singleton
    fun provideLoginRemoteRepository(composer: RemoteRepositoryComposer, retrofit: AuthRetrofit) =
            AuthRemoteRepository(composer, retrofit)

    @Provides
    @Singleton
    fun provideLoginInteractor(remoteRepository: AuthRemoteRepository, mixpanelAPI: MixpanelAPI) =
            AuthInteractor(remoteRepository, mixpanelAPI)
}