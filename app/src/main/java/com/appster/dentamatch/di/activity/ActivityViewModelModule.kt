package com.appster.dentamatch.di.activity

import android.arch.lifecycle.ViewModelProviders
import com.appster.dentamatch.di.ViewModelFactory
import com.appster.dentamatch.presentation.auth.*
import com.appster.dentamatch.presentation.calendar.SetAvailabilityActivity
import com.appster.dentamatch.presentation.calendar.SetAvailabilityViewModel
import com.appster.dentamatch.presentation.common.HomeActivity
import com.appster.dentamatch.presentation.common.HomeViewModel
import com.appster.dentamatch.presentation.common.SearchStateActivity
import com.appster.dentamatch.presentation.common.SearchStateViewModel
import com.appster.dentamatch.presentation.notification.NotificationActivity
import com.appster.dentamatch.presentation.notification.NotificationViewModel
import com.appster.dentamatch.presentation.profile.*
import com.appster.dentamatch.presentation.settings.SettingsViewModel
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationActivity
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationViewModel
import com.appster.dentamatch.presentation.profile.workexperience.*
import com.appster.dentamatch.presentation.searchjob.JobDetailActivity
import com.appster.dentamatch.presentation.searchjob.JobDetailViewModel
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

    @Provides
    fun provideSetAvailabilityViewModel(viewModelFactory: ViewModelFactory,
                                        setAvailabilityActivity: SetAvailabilityActivity): SetAvailabilityViewModel =
            ViewModelProviders.of(setAvailabilityActivity, viewModelFactory)
                    .get(SetAvailabilityViewModel::class.java)

    @Provides
    fun provideHomeViewModel(viewModelFactory: ViewModelFactory,
                             homeActivity: HomeActivity): HomeViewModel =
            ViewModelProviders.of(homeActivity, viewModelFactory)
                    .get(HomeViewModel::class.java)

    @Provides
    fun provideNotificationViewModel(viewModelFactory: ViewModelFactory,
                                     notificationActivity: NotificationActivity): NotificationViewModel =
            ViewModelProviders.of(notificationActivity, viewModelFactory)
                    .get(NotificationViewModel::class.java)

    @Provides
    fun provideSearchStateViewModel(viewModelFactory: ViewModelFactory,
                                    searchStateActivity: SearchStateActivity): SearchStateViewModel =
            ViewModelProviders.of(searchStateActivity, viewModelFactory)
                    .get(SearchStateViewModel::class.java)

    @Provides
    fun provideJobDetailViewModel(viewModelFactory: ViewModelFactory,
                                  jobDetailActivity: JobDetailActivity): JobDetailViewModel =
            ViewModelProviders.of(jobDetailActivity, viewModelFactory)
                    .get(JobDetailViewModel::class.java)

    @Provides
    fun provideAffiliationViewModel(viewModelFactory: ViewModelFactory,
                                    affiliationActivity: AffiliationActivity): AffiliationViewModel =
            ViewModelProviders.of(affiliationActivity, viewModelFactory)
                    .get(AffiliationViewModel::class.java)

    @Provides
    fun provideMyWorkExpListViewModel(viewModelFactory: ViewModelFactory,
                                      myWorkExpListActivity: MyWorkExpListActivity): MyWorkExpListViewModel =
            ViewModelProviders.of(myWorkExpListActivity, viewModelFactory)
                    .get(MyWorkExpListViewModel::class.java)

    @Provides
    fun provideSchoolingViewModel(viewModelFactory: ViewModelFactory,
                                  schoolingActivity: SchoolingActivity): SchoolingViewModel =
            ViewModelProviders.of(schoolingActivity, viewModelFactory)
                    .get(SchoolingViewModel::class.java)

    @Provides
    fun provideSkillsViewModel(viewModelFactory: ViewModelFactory,
                               skillsActivity: SkillsActivity): SkillsViewModel =
            ViewModelProviders.of(skillsActivity, viewModelFactory)
                    .get(SkillsViewModel::class.java)

    @Provides
    fun provideUpdateCertificateViewModel(viewModelFactory: ViewModelFactory,
                                          updateCertificateActivity: UpdateCertificateActivity): UpdateCertificateViewModel =
            ViewModelProviders.of(updateCertificateActivity, viewModelFactory)
                    .get(UpdateCertificateViewModel::class.java)

    @Provides
    fun provideUpdateLicenseViewModel(viewModelFactory: ViewModelFactory,
                                      updateLicenseActivity: UpdateLicenseActivity): UpdateLicenseViewModel =
            ViewModelProviders.of(updateLicenseActivity, viewModelFactory)
                    .get(UpdateLicenseViewModel::class.java)

    @Provides
    fun provideViewAndEditWorkExperienceViewModel(viewModelFactory: ViewModelFactory,
                                                  viewAndEditWorkExperienceActivity: ViewAndEditWorkExperienceActivity): ViewAndEditWorkExperienceViewModel =
            ViewModelProviders.of(viewAndEditWorkExperienceActivity, viewModelFactory)
                    .get(ViewAndEditWorkExperienceViewModel::class.java)

    @Provides
    fun provideWorkExperienceDetailViewModel(viewModelFactory: ViewModelFactory,
                                             workExperienceDetailActivity: WorkExperienceDetailActivity): WorkExperienceDetailViewModel =
            ViewModelProviders.of(workExperienceDetailActivity, viewModelFactory)
                    .get(WorkExperienceDetailViewModel::class.java)

    @Provides
    fun provideWorkExpListViewModel(viewModelFactory: ViewModelFactory,
                                    workExperienceDetailActivity: WorkExpListActivity): WorkExpListViewModel =
            ViewModelProviders.of(workExperienceDetailActivity, viewModelFactory)
                    .get(WorkExpListViewModel::class.java)

    @Provides
    fun provideAboutMeViewModel(viewModelFactory: ViewModelFactory,
                                aboutMeActivity: AboutMeActivity): AboutMeViewModel =
            ViewModelProviders.of(aboutMeActivity, viewModelFactory)
                    .get(AboutMeViewModel::class.java)

    @Provides
    fun provideCertificateViewModel(viewModelFactory: ViewModelFactory,
                                    certificateActivity: CertificateActivity): CertificateViewModel =
            ViewModelProviders.of(certificateActivity, viewModelFactory)
                    .get(CertificateViewModel::class.java)
}