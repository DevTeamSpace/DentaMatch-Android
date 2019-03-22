package com.appster.dentamatch.presentation.notification

import android.support.v7.util.DiffUtil
import com.appster.dentamatch.network.response.notification.NotificationData

class NotificationDiffCallback(
        private val oldList: List<NotificationData>,
        private val newList: List<NotificationData>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(p0: Int, p1: Int) = oldList[p0].id == newList[p1].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(p0: Int, p1: Int) = oldList[p0] == newList[p1]
}