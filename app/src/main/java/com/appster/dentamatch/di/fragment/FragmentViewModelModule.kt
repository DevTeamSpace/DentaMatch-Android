package com.appster.dentamatch.di.fragment

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
import com.appster.dentamatch.presentation.calendar.CalendarFragment
import com.appster.dentamatch.presentation.calendar.CalendarViewModel
import com.appster.dentamatch.presentation.messages.MessagesListFragment
import com.appster.dentamatch.presentation.messages.MessagesListViewModel
import com.appster.dentamatch.presentation.profile.ProfileFragment
import com.appster.dentamatch.presentation.profile.ProfileViewModel
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

}