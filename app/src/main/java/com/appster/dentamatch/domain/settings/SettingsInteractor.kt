package com.appster.dentamatch.domain.settings

import com.appster.dentamatch.model.UserModel
import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.reactivex.Completable

class SettingsInteractor(
        private val settingsRemoteRepository: SettingsRemoteRepository,
        private val mixpanelAPI: MixpanelAPI
) {

    companion object {
        const val EVENT_LOGOUT = "Logout"
    }

    fun updateUserLocation(userModel: UserModel): Completable =
            settingsRemoteRepository.updateUserLocation(userModel)
                    .ignoreElement()

    fun logout(): Completable = settingsRemoteRepository.logout()
            .doOnSuccess { mixpanelAPI.track(EVENT_LOGOUT) }
            .ignoreElement()
}