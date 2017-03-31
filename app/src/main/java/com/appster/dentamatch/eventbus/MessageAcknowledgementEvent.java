package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.ui.messages.Message;

/**
 * Created by Appster on 16/02/17.
 */

public class MessageAcknowledgementEvent {
    private Message mMessage;
    private String mRecruiterId;

    public MessageAcknowledgementEvent(Message message, String recruiterId){
        mMessage = message;
        mRecruiterId = recruiterId;
    }

    public String getmRecruiterId() {
        return mRecruiterId;
    }

    public Message getmMessage() {
        return mMessage;
    }
}
