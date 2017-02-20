package com.appster.dentamatch.model;

import com.appster.dentamatch.ui.messages.Message;

/**
 * Created by Appster on 16/02/17.
 */

public class MessageAcknowledgementEvent {
    private Message mMessage;

    public MessageAcknowledgementEvent(Message message){
        mMessage = message;
    }

    public Message getmMessage() {
        return mMessage;
    }
}
