package com.appster.dentamatch.domain.login

import com.appster.dentamatch.base.ApiErrorHandler
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class LoginModule {

    @Provides
    @Singleton
    fun provideLoginRetrofit(retrofit: Retrofit): LoginRetrofit = retrofit.create(LoginRetrofit::class.java)

    @Provides
    @Singleton
    fun provideLoginRemoteRepository(apiErrorHandler: ApiErrorHandler, retrofit: LoginRetrofit) =
            LoginRemoteRepository(apiErrorHandler, retrofit)

    @Provides
    @Singleton
    fun provideLoginInteractor(remoteRepository: LoginRemoteRepository, mixpanelAPI: MixpanelAPI) =
            LoginInteractor(remoteRepository, mixpanelAPI)
}