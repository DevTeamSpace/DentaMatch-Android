package com.appster.dentamatch.di

import com.appster.dentamatch.DentaApp
import com.appster.dentamatch.domain.auth.AuthModule
import com.appster.dentamatch.domain.calendar.CalendarModule
import com.appster.dentamatch.domain.chat.ChatModule
import com.appster.dentamatch.domain.common.CommonModule
import com.appster.dentamatch.domain.messages.MessagesModule
import com.appster.dentamatch.domain.notification.NotificationModule
import com.appster.dentamatch.domain.profile.ProfileModule
import com.appster.dentamatch.domain.searchjob.SearchJobModule
import com.appster.dentamatch.domain.settings.SettingsModule
import com.appster.dentamatch.domain.tracks.TracksModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
        RetrofitCreatorModule::class, ViewModelModule::class, AuthModule::class,
        SettingsModule::class, SearchJobModule::class, ProfileModule::class,
        CalendarModule::class, CommonModule::class, NotificationModule::class,
        MessagesModule::class, TracksModule::class, ChatModule::class]
)
interface ApplicationComponent : AndroidInjector<DentaApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DentaApp>()
}