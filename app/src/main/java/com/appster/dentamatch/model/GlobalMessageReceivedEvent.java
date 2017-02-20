package com.appster.dentamatch.model;

import com.appster.dentamatch.ui.messages.ChatMessageModel;

/**
 * Created by Appster on 13/02/17.
 */

public class GlobalMessageReceivedEvent {
    private ChatMessageModel model;

    public GlobalMessageReceivedEvent(ChatMessageModel model) {
        this.model = model;
    }

    public ChatMessageModel getModel() {
        return model;
    }
}
