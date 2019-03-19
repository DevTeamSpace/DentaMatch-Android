package com.appster.dentamatch.presentation.messages

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.chat.DBHelper
import com.appster.dentamatch.chat.DBModel
import com.appster.dentamatch.domain.chat.ChatInteractor
import com.appster.dentamatch.eventbus.ChatHistoryRetrievedEvent
import com.appster.dentamatch.eventbus.ChatPersonalMessageReceivedEvent
import com.appster.dentamatch.eventbus.MessageAcknowledgementEvent
import com.appster.dentamatch.model.ChatResponse
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.LogUtils
import com.appster.dentamatch.util.PreferenceUtil
import com.appster.dentamatch.util.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import timber.log.Timber

class ChatViewModel(
        private val chatInteractor: ChatInteractor
) : BaseLoadingViewModel() {

    var recruiterId: String = ""
    var userId: String = PreferenceUtil.getUserChatId()

    private val mutableDBModel = MutableLiveData<ChatResponse>()
    private val mutableInitError = MutableLiveData<Throwable>()

    val bdModel: LiveData<ChatResponse> get() = mutableDBModel
    val initError: LiveData<Throwable> get() = mutableInitError

    fun setArguments(intent: Intent) {
        if (intent.hasExtra(Constants.EXTRA_CHAT_MODEL)) {
            recruiterId = intent.getStringExtra(Constants.EXTRA_CHAT_MODEL) ?: ""
        }
    }

    fun initChat() {
        addDisposable(
                chatInteractor.initChat(recruiterId)
                        .compose(viewModelCompose())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError { mutableInitError.postValue(it) }
                        .subscribe({ mutableDBModel.postValue(it) }, { Timber.e(it) })
        )
    }
}