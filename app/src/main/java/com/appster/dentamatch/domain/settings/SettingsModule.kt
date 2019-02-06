package com.appster.dentamatch.domain.settings

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class SettingsModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): SettingsRetrofit = retrofit.create(SettingsRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(composer: RemoteRepositoryComposer, settingsRetrofit:SettingsRetrofit) =
            SettingsRemoteRepository(composer, settingsRetrofit)

    @Provides
    @Singleton
    fun provideInteractor(settingsRemoteRepository: SettingsRemoteRepository, mixpanelAPI: MixpanelAPI) =
            SettingsInteractor(settingsRemoteRepository, mixpanelAPI)
}