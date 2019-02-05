package com.appster.dentamatch.di.activity

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
import com.appster.dentamatch.presentation.auth.LoginViewModel
import com.appster.dentamatch.ui.auth.LoginActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityViewModelModule {

    @Provides
    fun provideLoginViewModel(viewModelFactory: ViewModelFactory, loginActivity: LoginActivity): LoginViewModel =
            ViewModelProviders.of(loginActivity, viewModelFactory).get(LoginViewModel::class.java)
}