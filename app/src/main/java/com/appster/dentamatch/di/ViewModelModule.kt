package com.appster.dentamatch.di

import android.arch.lifecycle.ViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import com.appster.dentamatch.domain.calendar.CalendarInteractor
import com.appster.dentamatch.domain.common.CommonInteractor
import com.appster.dentamatch.domain.messages.MessagesInteractor
import com.appster.dentamatch.domain.notification.NotificationInteractor
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.domain.settings.SettingsInteractor
import com.appster.dentamatch.presentation.auth.ForgotPasswordViewModel
import com.appster.dentamatch.presentation.auth.LoginViewModel
import com.appster.dentamatch.presentation.auth.ResetPasswordViewModel
import com.appster.dentamatch.presentation.calendar.CalendarViewModel
import com.appster.dentamatch.presentation.calendar.SetAvailabilityViewModel
import com.appster.dentamatch.presentation.common.HomeViewModel
import com.appster.dentamatch.presentation.common.SearchStateViewModel
import com.appster.dentamatch.presentation.messages.MessagesListViewModel
import com.appster.dentamatch.presentation.notification.NotificationViewModel
import com.appster.dentamatch.presentation.profile.CreateProfileViewModel
import com.appster.dentamatch.presentation.profile.ProfileViewModel
import com.appster.dentamatch.presentation.searchjob.SelectJobTitleViewModel
import com.appster.dentamatch.presentation.settings.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
@Suppress("unused")
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(authInteractor: AuthInteractor): ViewModel =
            LoginViewModel(authInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsViewModel(settingsInteractor: SettingsInteractor): ViewModel =
            SettingsViewModel(settingsInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SelectJobTitleViewModel::class)
    fun provideSelectJobTitleViewModel(searchJobInteractor: SearchJobInteractor): ViewModel =
            SelectJobTitleViewModel(searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(CreateProfileViewModel::class)
    fun provideCreateProfileViewModel(profileInteractor: ProfileInteractor): ViewModel =
            CreateProfileViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun provideProfileViewModel(profileInteractor: ProfileInteractor): ViewModel =
            ProfileViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    fun provideResetPasswordViewModel(authInteractor: AuthInteractor): ViewModel =
            ResetPasswordViewModel(authInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    fun provideForgotPasswordViewModel(authInteractor: AuthInteractor): ViewModel =
            ForgotPasswordViewModel(authInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    fun provideCalendarViewModel(calendarInteractor: CalendarInteractor): ViewModel =
            CalendarViewModel(calendarInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SetAvailabilityViewModel::class)
    fun provideSetAvailabilityViewModel(calendarInteractor: CalendarInteractor): ViewModel =
            SetAvailabilityViewModel(calendarInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun provideHomeViewModel(commonInteractor: CommonInteractor,
                             profileInteractor: ProfileInteractor): ViewModel =
            HomeViewModel(commonInteractor, profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    fun provideNotificationViewModel(notificationInteractor: NotificationInteractor): ViewModel =
            NotificationViewModel(notificationInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SearchStateViewModel::class)
    fun provideSearchStateViewModel(commonInteractor: CommonInteractor): ViewModel =
            SearchStateViewModel(commonInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(MessagesListViewModel::class)
    fun provideMessagesListViewModel(messagesInteractor: MessagesInteractor): ViewModel =
            MessagesListViewModel(messagesInteractor)
}