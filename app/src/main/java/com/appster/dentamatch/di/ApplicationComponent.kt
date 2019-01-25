package com.appster.dentamatch.di

import com.appster.dentamatch.DentaApp
import com.appster.dentamatch.domain.login.LoginModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
        RetrofitCreatorModule::class, ViewModelModule::class, LoginModule::class]
)
interface ApplicationComponent : AndroidInjector<DentaApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DentaApp>()
}