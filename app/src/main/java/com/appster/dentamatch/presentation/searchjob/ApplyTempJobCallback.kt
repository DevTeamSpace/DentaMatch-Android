package com.appster.dentamatch.presentation.searchjob

interface ApplyTempJobCallback {

    fun onDatesSelected(notificationId: Int, dates: ArrayList<String>)
}