package com.appster.dentamatch.presentation.searchjob

import android.support.v7.util.DiffUtil

class ApplyTempJobItemCallback : DiffUtil.ItemCallback<ApplyTempJobModel>() {
    override fun areItemsTheSame(p0: ApplyTempJobModel, p1: ApplyTempJobModel) = p0 == p1

    override fun areContentsTheSame(p0: ApplyTempJobModel, p1: ApplyTempJobModel) =
            p0.day == p1.day && p0.date == p1.date && p0.checked == p1.checked
}