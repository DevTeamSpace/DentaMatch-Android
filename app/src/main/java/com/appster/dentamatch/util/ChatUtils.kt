package com.appster.dentamatch.util

import android.content.Context
import android.content.Intent
import com.appster.dentamatch.presentation.messages.ChatActivity


fun startChatWithUser(context: Context, userId: String) =
        Intent(context, ChatActivity::class.java).apply {
            putExtra(Constants.EXTRA_CHAT_MODEL, userId)
            context.startActivity(this)
        }
