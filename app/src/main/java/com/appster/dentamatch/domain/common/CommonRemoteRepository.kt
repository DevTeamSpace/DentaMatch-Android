package com.appster.dentamatch.domain.common

import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.request.Notification.UpdateFcmTokenRequest
import com.appster.dentamatch.network.response.profile.StateResponse
import io.reactivex.Single

class CommonRemoteRepository(
        private val retrofit: CommonRetrofit,
        private val composer: RemoteRepositoryComposer
) {

    fun updateFcmToken(fcmToken: String): Single<BaseResponse> =
            retrofit.updateFcmToken(UpdateFcmTokenRequest().apply { setUpdateDeviceToken(fcmToken) })
                    .compose(composer.apiCompose())

    fun requestStateList(): Single<StateResponse> =
            retrofit.requestStateList()
                    .compose(composer.apiCompose())
}