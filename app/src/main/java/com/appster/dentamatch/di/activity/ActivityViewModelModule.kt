package com.appster.dentamatch.di.activity

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
import com.appster.dentamatch.presentation.auth.*
import com.appster.dentamatch.presentation.settings.SettingsViewModel
import com.appster.dentamatch.presentation.profile.CreateProfileActivity
import com.appster.dentamatch.presentation.profile.CreateProfileViewModel
import com.appster.dentamatch.presentation.searchjob.SelectJobTitleActivity
import com.appster.dentamatch.presentation.searchjob.SelectJobTitleViewModel
import com.appster.dentamatch.presentation.settings.SettingsActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityViewModelModule {

    @Provides
    fun provideLoginViewModel(viewModelFactory: ViewModelFactory,
                              loginActivity: LoginActivity): LoginViewModel =
            ViewModelProviders.of(loginActivity, viewModelFactory).get(LoginViewModel::class.java)

    @Provides
    fun provideSettingsViewModel(viewModelFactory: ViewModelFactory,
                                 settingsActivity: SettingsActivity): SettingsViewModel =
            ViewModelProviders.of(settingsActivity, viewModelFactory).get(SettingsViewModel::class.java)

    @Provides
    fun provideSelectJobTitleViewModel(viewModelFactory: ViewModelFactory,
                                       selectJobTitleActivity: SelectJobTitleActivity): SelectJobTitleViewModel =
            ViewModelProviders.of(selectJobTitleActivity, viewModelFactory)
                    .get(SelectJobTitleViewModel::class.java)

    @Provides
    fun provideCreateProfileViewModel(viewModelFactory: ViewModelFactory,
                                      createProfileActivity: CreateProfileActivity): CreateProfileViewModel =
            ViewModelProviders.of(createProfileActivity, viewModelFactory)
                    .get(CreateProfileViewModel::class.java)

    @Provides
    fun provideResetPasswordViewModel(viewModelFactory: ViewModelFactory,
                                      resetPasswordActivity: ResetPasswordActivity): ResetPasswordViewModel =
            ViewModelProviders.of(resetPasswordActivity, viewModelFactory)
                    .get(ResetPasswordViewModel::class.java)

    @Provides
    fun provideForgotPasswordViewModel(viewModelFactory: ViewModelFactory,
                                       forgotPasswordActivity: ForgotPasswordActivity): ForgotPasswordViewModel =
            ViewModelProviders.of(forgotPasswordActivity, viewModelFactory)
                    .get(ForgotPasswordViewModel::class.java)
}