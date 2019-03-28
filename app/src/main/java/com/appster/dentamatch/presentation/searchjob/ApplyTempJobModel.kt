package com.appster.dentamatch.presentation.searchjob

data class ApplyTempJobModel(
        var day: String,
        var date: String,
        var rawDateString: String,
        var checked: Boolean = true
)