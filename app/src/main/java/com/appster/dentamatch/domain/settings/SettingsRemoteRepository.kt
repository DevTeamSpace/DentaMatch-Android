package com.appster.dentamatch.domain.settings

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.model.UserModel
import com.appster.dentamatch.network.request.auth.ChangeUserLocation
import io.reactivex.Single

class SettingsRemoteRepository(
        private val composer: RemoteRepositoryComposer,
        private val settingsRetrofit: SettingsRetrofit
) {

    fun updateUserLocation(userModel: UserModel): Single<BaseResponse> =
            settingsRetrofit.updateUserLocation(createLocationRequest(userModel))
                    .compose(composer.apiCompose())

    fun logout(): Single<BaseResponse> = settingsRetrofit.logout()
            .compose(composer.apiCompose())

    private fun createLocationRequest(userModel: UserModel) =
        ChangeUserLocation().apply {
            setLatitude(userModel.latitude)
            setLongitude(userModel.longitude)
            setPreferredLocation(userModel.preferredJobLocation)
            setZipCode(Integer.valueOf(userModel.postalCode))
            setPreferredCity(userModel.preferredCity)
            setPreferredState(userModel.preferredState)
            setPreferredCountry(userModel.preferredCountry)
        }
}