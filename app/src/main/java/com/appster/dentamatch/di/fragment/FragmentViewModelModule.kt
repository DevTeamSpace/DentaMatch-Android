package com.appster.dentamatch.di.fragment

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
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
}