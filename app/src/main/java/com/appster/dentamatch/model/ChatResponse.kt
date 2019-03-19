package com.appster.dentamatch.model

import android.os.Parcelable
import com.appster.dentamatch.base.BaseResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatResponse(
        var result: ChatListModel? = null
) : BaseResponse(), Parcelable