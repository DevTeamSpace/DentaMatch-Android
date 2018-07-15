package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.ui.messages.Message;

/**
 * Created by Appster on 16/02/17.
 * To stream message acknowledged event.
 */

public class MessageAcknowledgementEvent {
    private final Message mMessage;
    private final String mRecruiterId;

    public MessageAcknowledgementEvent(Message message, String recruiterId) {
        mMessage = message;
        mRecruiterId = recruiterId;
    }

    public String getRecruiterId() {
        return mRecruiterId;
    }

    public Message getMessage() {
        return mMessage;
    }
}
