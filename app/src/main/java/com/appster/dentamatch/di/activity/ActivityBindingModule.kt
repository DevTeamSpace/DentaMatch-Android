package com.appster.dentamatch.di.activity

import com.appster.dentamatch.di.fragment.FragmentBindingModule
import com.appster.dentamatch.presentation.auth.ForgotPasswordActivity
import com.appster.dentamatch.presentation.auth.LoginActivity
import com.appster.dentamatch.presentation.auth.ResetPasswordActivity
import com.appster.dentamatch.presentation.auth.UserVerifyPendingActivity
import com.appster.dentamatch.presentation.calendar.SetAvailabilityActivity
import com.appster.dentamatch.presentation.common.HomeActivity
import com.appster.dentamatch.presentation.common.ImageViewingActivity
import com.appster.dentamatch.presentation.common.SearchStateActivity
import com.appster.dentamatch.presentation.common.SplashActivity
import com.appster.dentamatch.presentation.map.PlacesMapActivity
import com.appster.dentamatch.presentation.messages.ChatActivity
import com.appster.dentamatch.presentation.notification.NotificationActivity
import com.appster.dentamatch.presentation.onboardtutorial.OnBoardingActivity
import com.appster.dentamatch.presentation.profile.*
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationActivity
import com.appster.dentamatch.presentation.profile.workexperience.*
import com.appster.dentamatch.presentation.searchjob.*
import com.appster.dentamatch.presentation.settings.SettingsActivity
import com.appster.dentamatch.presentation.termsnprivacy.TermsAndConditionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindResetPasswordActivity(): ResetPasswordActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindUserVerifyPendingActivity(): UserVerifyPendingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSetAvailabilityActivity(): SetAvailabilityActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindPlacesMapActivityActivity(): PlacesMapActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindChatActivity(): ChatActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindNotificationActivity(): NotificationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindOnBoardingActivity(): OnBoardingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindAffiliationActivity(): AffiliationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindMyWorkExpListActivity(): MyWorkExpListActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSchoolingActivity(): SchoolingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSkillsActivity(): SkillsActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSubSkillsActivity(): SubSkillsActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindUpdateCertificateActivity(): UpdateCertificateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindUpdateLicenseActivity(): UpdateLicenseActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindViewAndEditWorkExperienceActivity(): ViewAndEditWorkExperienceActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindWorkExperienceActivity(): WorkExperienceActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindWorkExperienceDetailActivity(): WorkExperienceDetailActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindWorkExpListActivity(): WorkExpListActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindAboutMeActivity(): AboutMeActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindCertificateActivity(): CertificateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindCreateProfileActivity1(): CreateProfileActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindProfileCompletedPendingActivity(): ProfileCompletedPendingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindUpdateProfileActivity(): UpdateProfileActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindCompleteProfileDialogActivity(): CompleteProfileDialogActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindJobDetailActivity(): JobDetailActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSearchJobActivity(): SearchJobActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSelectJobTitleActivity(): SelectJobTitleActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSelectPreferredLocationActivity(): SelectPreferredLocationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSettingActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindTermsAndConditionActivity(): TermsAndConditionActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindImageViewingActivity(): ImageViewingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSearchStateActivity(): SearchStateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class, FragmentBindingModule::class])
    abstract fun bindSplashActivity(): SplashActivity
}