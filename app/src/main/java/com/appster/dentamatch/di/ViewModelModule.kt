package com.appster.dentamatch.di

import android.arch.lifecycle.ViewModel
import com.appster.dentamatch.domain.auth.AuthInteractor
import com.appster.dentamatch.domain.calendar.CalendarInteractor
import com.appster.dentamatch.domain.chat.ChatInteractor
import com.appster.dentamatch.domain.common.CommonInteractor
import com.appster.dentamatch.domain.messages.MessagesInteractor
import com.appster.dentamatch.domain.notification.NotificationInteractor
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.domain.searchjob.SearchJobInteractor
import com.appster.dentamatch.domain.settings.SettingsInteractor
import com.appster.dentamatch.domain.tracks.TracksInteractor
import com.appster.dentamatch.presentation.auth.ForgotPasswordViewModel
import com.appster.dentamatch.presentation.auth.LoginViewModel
import com.appster.dentamatch.presentation.auth.ResetPasswordViewModel
import com.appster.dentamatch.presentation.calendar.CalendarViewModel
import com.appster.dentamatch.presentation.calendar.SetAvailabilityViewModel
import com.appster.dentamatch.presentation.common.HomeViewModel
import com.appster.dentamatch.presentation.common.SearchStateViewModel
import com.appster.dentamatch.presentation.messages.ChatViewModel
import com.appster.dentamatch.presentation.messages.MessagesListViewModel
import com.appster.dentamatch.presentation.notification.NotificationViewModel
import com.appster.dentamatch.presentation.profile.AboutMeViewModel
import com.appster.dentamatch.presentation.profile.CertificateViewModel
import com.appster.dentamatch.presentation.profile.CreateProfileViewModel
import com.appster.dentamatch.presentation.profile.ProfileViewModel
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationViewModel
import com.appster.dentamatch.presentation.profile.workexperience.*
import com.appster.dentamatch.presentation.searchjob.JobDetailViewModel
import com.appster.dentamatch.presentation.searchjob.JobListViewModel
import com.appster.dentamatch.presentation.searchjob.JobMapViewModel
import com.appster.dentamatch.presentation.searchjob.SelectJobTitleViewModel
import com.appster.dentamatch.presentation.settings.SettingsViewModel
import com.appster.dentamatch.presentation.tracks.AppliedJobsViewModel
import com.appster.dentamatch.presentation.tracks.SavedJobViewModel
import com.appster.dentamatch.presentation.tracks.ShortlistedJobsViewModel
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
                             profileInteractor: ProfileInteractor,
                             messagesInteractor: MessagesInteractor): ViewModel =
            HomeViewModel(commonInteractor, profileInteractor, messagesInteractor)

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

    @Provides
    @IntoMap
    @ViewModelKey(ShortlistedJobsViewModel::class)
    fun provideShortlistedJobsViewModel(tracksInteractor: TracksInteractor,
                                        searchJobInteractor: SearchJobInteractor): ViewModel =
            ShortlistedJobsViewModel(tracksInteractor, searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(AppliedJobsViewModel::class)
    fun provideAppliedJobsViewModel(tracksInteractor: TracksInteractor,
                                    searchJobInteractor: SearchJobInteractor): ViewModel =
            AppliedJobsViewModel(tracksInteractor, searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(JobDetailViewModel::class)
    fun provideJobDetailViewModel(searchJobInteractor: SearchJobInteractor): ViewModel =
            JobDetailViewModel(searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(AffiliationViewModel::class)
    fun provideAffiliationViewModel(profileInteractor: ProfileInteractor): ViewModel =
            AffiliationViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(MyWorkExpListViewModel::class)
    fun provideMyWorkExpListViewModel(profileInteractor: ProfileInteractor): ViewModel =
            MyWorkExpListViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SchoolingViewModel::class)
    fun provideSchoolingViewModel(profileInteractor: ProfileInteractor): ViewModel =
            SchoolingViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SkillsViewModel::class)
    fun provideSkillsViewModel(profileInteractor: ProfileInteractor): ViewModel =
            SkillsViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(UpdateCertificateViewModel::class)
    fun provideUpdateCertificateViewModel(profileInteractor: ProfileInteractor): ViewModel =
            UpdateCertificateViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(UpdateLicenseViewModel::class)
    fun provideUpdateLicenseViewModel(profileInteractor: ProfileInteractor): ViewModel =
            UpdateLicenseViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(ViewAndEditWorkExperienceViewModel::class)
    fun provideViewAndEditWorkExperienceViewModel(profileInteractor: ProfileInteractor): ViewModel =
            ViewAndEditWorkExperienceViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(WorkExperienceDetailViewModel::class)
    fun provideWorkExperienceDetailViewModel(profileInteractor: ProfileInteractor): ViewModel =
            WorkExperienceDetailViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(WorkExpListViewModel::class)
    fun provideWorkExpListViewModel(profileInteractor: ProfileInteractor): ViewModel =
            WorkExpListViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(AboutMeViewModel::class)
    fun provideAboutMeViewModel(profileInteractor: ProfileInteractor): ViewModel =
            AboutMeViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(CertificateViewModel::class)
    fun provideCertificateViewModel(profileInteractor: ProfileInteractor): ViewModel =
            CertificateViewModel(profileInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(JobMapViewModel::class)
    fun provideJobMapViewModel(searchJobInteractor: SearchJobInteractor): ViewModel =
            JobMapViewModel(searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(JobListViewModel::class)
    fun provideJobListViewModel(searchJobInteractor: SearchJobInteractor): ViewModel =
            JobListViewModel(searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(SavedJobViewModel::class)
    fun provideSavedJobViewModel(searchJobInteractor: SearchJobInteractor): ViewModel =
            SavedJobViewModel(searchJobInteractor)

    @Provides
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun provideChatViewModel(chatInteractor: ChatInteractor): ViewModel =
            ChatViewModel(chatInteractor)
}