package com.appster.dentamatch.di

import android.content.Context
import com.appster.dentamatch.BuildConfig
import com.appster.dentamatch.DentaApp
import com.appster.dentamatch.di.activity.ActivityBindingModule
import com.appster.dentamatch.di.fragment.FragmentBindingModule
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ActivityBindingModule::class, FragmentBindingModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: DentaApp): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideMixpanelApi(context: Context): MixpanelAPI =
            MixpanelAPI.getInstance(context, BuildConfig.MixpanelKey)
}