package com.appster.dentamatch.network.request.Notification

import java.util.ArrayList

data class DeleteNotificationRequest(
        var notificationIds: ArrayList<Int> = ArrayList()
)