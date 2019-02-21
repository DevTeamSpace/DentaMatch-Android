package com.appster.dentamatch.di.fragment

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
import com.appster.dentamatch.presentation.calendar.CalendarFragment
import com.appster.dentamatch.presentation.calendar.CalendarViewModel
import com.appster.dentamatch.presentation.messages.MessagesListFragment
import com.appster.dentamatch.presentation.messages.MessagesListViewModel
import com.appster.dentamatch.presentation.profile.ProfileFragment
import com.appster.dentamatch.presentation.profile.ProfileViewModel
import com.appster.dentamatch.presentation.searchjob.JobListFragment
import com.appster.dentamatch.presentation.searchjob.JobListViewModel
import com.appster.dentamatch.presentation.searchjob.JobMapFragment
import com.appster.dentamatch.presentation.searchjob.JobMapViewModel
import com.appster.dentamatch.presentation.tracks.*
import dagger.Module
import dagger.Provides

@Module
class FragmentViewModelModule {

    @Provides
    fun provideProfileViewModel(viewModelFactory: ViewModelFactory,
                                profileFragment: ProfileFragment): ProfileViewModel =
            ViewModelProviders.of(profileFragment, viewModelFactory).get(ProfileViewModel::class.java)

    @Provides
    fun provideCalendarViewModel(viewModelFactory: ViewModelFactory,
                                 calendarFragment: CalendarFragment): CalendarViewModel =
            ViewModelProviders.of(calendarFragment, viewModelFactory).get(CalendarViewModel::class.java)

    @Provides
    fun provideMessagesListViewModel(viewModelFactory: ViewModelFactory,
                                     messagesListFragment: MessagesListFragment): MessagesListViewModel =
            ViewModelProviders.of(messagesListFragment, viewModelFactory)
                    .get(MessagesListViewModel::class.java)

    @Provides
    fun provideAppliedJobsViewModel(viewModelFactory: ViewModelFactory,
                                    appliedJobsFragment: AppliedJobsFragment): AppliedJobsViewModel =
            ViewModelProviders.of(appliedJobsFragment, viewModelFactory)
                    .get(AppliedJobsViewModel::class.java)

    @Provides
    fun provideShortlistedJobsViewModel(viewModelFactory: ViewModelFactory,
                                        shortlistedJobsFragment: ShortlistedJobsFragment): ShortlistedJobsViewModel =
            ViewModelProviders.of(shortlistedJobsFragment, viewModelFactory)
                    .get(ShortlistedJobsViewModel::class.java)

    @Provides
    fun provideJobMapViewModel(viewModelFactory: ViewModelFactory,
                               jobMapFragment: JobMapFragment): JobMapViewModel =
            ViewModelProviders.of(jobMapFragment, viewModelFactory)
                    .get(JobMapViewModel::class.java)

    @Provides
    fun provideJobListViewModel(viewModelFactory: ViewModelFactory,
                                jobListFragment: JobListFragment): JobListViewModel =
            ViewModelProviders.of(jobListFragment, viewModelFactory)
                    .get(JobListViewModel::class.java)

    @Provides
    fun provideSavedJobViewModel(viewModelFactory: ViewModelFactory,
                                 savedJobFragment: SavedJobFragment): SavedJobViewModel =
            ViewModelProviders.of(savedJobFragment, viewModelFactory)
                    .get(SavedJobViewModel::class.java)
}