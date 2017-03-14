package com.appster.dentamatch.EventBus;

import com.appster.dentamatch.ui.messages.ChatMessageModel;

/**
 * Created by Appster on 09/02/17.
 */

public class ChatPersonalMessageReceivedEvent {
    private ChatMessageModel model;

    public ChatPersonalMessageReceivedEvent(ChatMessageModel model) {
        this.model = model;
    }

    public ChatMessageModel getModel() {
        return model;
    }
}
