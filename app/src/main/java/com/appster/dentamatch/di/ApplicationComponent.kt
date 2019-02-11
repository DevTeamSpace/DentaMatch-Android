package com.appster.dentamatch.di

import com.appster.dentamatch.DentaApp
import com.appster.dentamatch.domain.auth.AuthModule
import com.appster.dentamatch.domain.profile.ProfileModule
import com.appster.dentamatch.domain.searchjob.SearchJobModule
import com.appster.dentamatch.domain.settings.SettingsModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
        RetrofitCreatorModule::class, ViewModelModule::class, AuthModule::class,
        SettingsModule::class, SearchJobModule::class, ProfileModule::class]
)
interface ApplicationComponent : AndroidInjector<DentaApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DentaApp>()
}