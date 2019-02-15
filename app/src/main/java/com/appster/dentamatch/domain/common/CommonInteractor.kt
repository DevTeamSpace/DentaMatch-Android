package com.appster.dentamatch.domain.common

import com.appster.dentamatch.network.response.profile.StateResponse
import io.reactivex.Completable
import io.reactivex.Single

class CommonInteractor(
        private val remoteRepository: CommonRemoteRepository
) {

    fun updateFcmToken(fcmToken: String): Completable =
            remoteRepository.updateFcmToken(fcmToken)
                    .ignoreElement()

    fun requestStateList(): Single<StateResponse> =
            remoteRepository.requestStateList()
}