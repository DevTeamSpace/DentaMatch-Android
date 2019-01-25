package com.appster.dentamatch.di.fragment

import com.appster.dentamatch.ui.calendar.CalendarFragment
import com.appster.dentamatch.ui.messages.MessagesListFragment
import com.appster.dentamatch.ui.profile.ProfileFragment
import com.appster.dentamatch.ui.searchjob.JobListFragment
import com.appster.dentamatch.ui.searchjob.JobMapFragment
import com.appster.dentamatch.ui.searchjob.JobsFragment
import com.appster.dentamatch.ui.tracks.AppliedJobsFragment
import com.appster.dentamatch.ui.tracks.SavedJobFragment
import com.appster.dentamatch.ui.tracks.ShortlistedJobsFragment
import com.appster.dentamatch.ui.tracks.TrackFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class FragmentBindingModule {

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindCalendarFragment(): CalendarFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindMessagesListFragment(): MessagesListFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindJobListFragment(): JobListFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindJobMapFragment(): JobMapFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindJobsFragment(): JobsFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindAppliedJobsFragment(): AppliedJobsFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindSavedJobFragment(): SavedJobFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindShortlistedJobsFragment(): ShortlistedJobsFragment

    @ContributesAndroidInjector(modules = [FragmentViewModelModule::class])
    abstract fun bindTrackFragment(): TrackFragment
}