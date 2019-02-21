package com.appster.dentamatch.di

import android.content.Context
import com.appster.dentamatch.BuildConfig
import com.appster.dentamatch.DentaApp
import com.appster.dentamatch.base.ApiErrorHandler
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.di.activity.ActivityBindingModule
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityBindingModule::class])
class ApplicationModule {

    @Provides
    fun provideContext(application: DentaApp): Context = application.applicationContext

    @Provides
    fun provideMixpanelApi(context: Context): MixpanelAPI =
            MixpanelAPI.getInstance(context, BuildConfig.MixpanelKey)

    @Provides
    fun provideRemoteRepositoryComposer(apiErrorHandler: ApiErrorHandler) =
            RemoteRepositoryComposer(apiErrorHandler)
}