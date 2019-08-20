package com.appster.dentamatch.util

import android.content.Context
import android.content.Intent
import com.appster.dentamatch.presentation.messages.ChatActivity
import org.jetbrains.annotations.NotNull


fun startChatWithUser(context: Context, userId: String): Intent? =
        Intent(context, ChatActivity::class.java).apply {
            putExtra(Constants.EXTRA_CHAT_MODEL, userId)
            context.startActivity(this)
        }