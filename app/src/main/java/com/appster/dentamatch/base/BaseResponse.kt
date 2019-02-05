package com.appster.dentamatch.base

open class BaseResponse {
    fun hasError(): Boolean = statusCode >= 400

    var status: Int = 0
    var statusCode: Int = 0
    var message: String = ""
}