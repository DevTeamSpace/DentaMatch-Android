package com.appster.dentamatch.domain.settings

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SettingsModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): SettingsRetrofit = retrofit.create(SettingsRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(composer: RemoteRepositoryComposer, settingsRetrofit:SettingsRetrofit) =
            SettingsRemoteRepository(composer, settingsRetrofit)

    @Provides
    fun provideInteractor(settingsRemoteRepository: SettingsRemoteRepository, mixpanelAPI: MixpanelAPI) =
            SettingsInteractor(settingsRemoteRepository, mixpanelAPI)
}