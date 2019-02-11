package com.appster.dentamatch.base

import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

class RemoteRepositoryComposer(private val apiErrorHandler: ApiErrorHandler) {

    fun <T : BaseResponse> apiCompose(): SingleTransformer<T, T> {
        return SingleTransformer { single ->
            single.onErrorResumeNext(apiErrorHandler.handleError())
                    .subscribeOn(Schedulers.io())
                    .doOnSuccess(::throwIfError)
        }
    }

    @Throws(Exception::class)
    private fun throwIfError(baseResponse: BaseResponse) {
        if (baseResponse.hasError()) {
            throw RuntimeException(baseResponse.message)
        }
    }
}