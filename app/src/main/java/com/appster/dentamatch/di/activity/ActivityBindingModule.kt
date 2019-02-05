package com.appster.dentamatch.di.activity

import com.appster.dentamatch.ui.auth.ForgotPasswordActivity
import com.appster.dentamatch.ui.auth.LoginActivity
import com.appster.dentamatch.ui.auth.ResetPasswordActivity
import com.appster.dentamatch.ui.auth.UserVerifyPendingActivity
import com.appster.dentamatch.ui.calendar.SetAvailabilityActivity
import com.appster.dentamatch.ui.common.HomeActivity
import com.appster.dentamatch.ui.common.ImageViewingActivity
import com.appster.dentamatch.ui.common.SearchStateActivity
import com.appster.dentamatch.ui.common.SplashActivity
import com.appster.dentamatch.ui.map.PlacesMapActivity
import com.appster.dentamatch.ui.messages.ChatActivity
import com.appster.dentamatch.ui.notification.NotificationActivity
import com.appster.dentamatch.ui.onboardtutorial.OnBoardingActivity
import com.appster.dentamatch.ui.profile.*
import com.appster.dentamatch.ui.profile.affiliation.AffiliationActivity
import com.appster.dentamatch.ui.profile.workexperience.*
import com.appster.dentamatch.ui.searchjob.*
import com.appster.dentamatch.ui.settings.SettingActivity
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindResetPasswordActivity(): ResetPasswordActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindUserVerifyPendingActivity(): UserVerifyPendingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSetAvailabilityActivity(): SetAvailabilityActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindPlacesMapActivityActivity(): PlacesMapActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindChatActivity(): ChatActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindNotificationActivity(): NotificationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindOnBoardingActivity(): OnBoardingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindAffiliationActivity(): AffiliationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindMyWorkExpListActivity(): MyWorkExpListActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSchoolingActivity(): SchoolingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSkillsActivity(): SkillsActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSubSkillsActivity(): SubSkillsActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindUpdateCertificateActivity(): UpdateCertificateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindUpdateLicenseActivity(): UpdateLicenseActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindViewAndEditWorkExperienceActivity(): ViewAndEditWorkExperienceActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindWorkExperienceActivity(): WorkExperienceActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindWorkExperienceDetailActivity(): WorkExperienceDetailActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindWorkExpListActivity(): WorkExpListActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindAboutMeActivity(): AboutMeActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindCertificateActivity(): CertificateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindCreateProfileActivity1(): CreateProfileActivity1

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindCreateProfileActivity2(): CreateProfileActivity2

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindProfileCompletedPendingActivity(): ProfileCompletedPendingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindUpdateProfileActivity(): UpdateProfileActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindCompleteProfileDialogActivity(): CompleteProfileDialogActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindJobDetailActivity(): JobDetailActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSearchJobActivity(): SearchJobActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSelectJobTitleActivity(): SelectJobTitleActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSelectPreferredLocationActivity(): SelectPreferredLocationActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSettingActivity(): SettingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindTermsAndConditionActivity(): TermsAndConditionActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindImageViewingActivity(): ImageViewingActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSearchStateActivity(): SearchStateActivity

    @ContributesAndroidInjector(modules = [ActivityViewModelModule::class])
    abstract fun bindSplashActivity(): SplashActivity
}