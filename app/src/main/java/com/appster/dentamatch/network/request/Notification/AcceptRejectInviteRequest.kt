/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.Notification

/**
 * Created by virender on 27/02/17.
 * To inject activity reference.
 */
data class AcceptRejectInviteRequest(
    var notificationId: Int = 0,
    var acceptStatus: Int = 0,
    var jobDates: ArrayList<String>? = null
)
