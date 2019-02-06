/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.presentation.messages.ChatMessageModel;

/**
 * Created by Appster on 09/02/17.
 * To stream personal received chat messages.
 */

public class ChatPersonalMessageReceivedEvent {
    private final ChatMessageModel model;

    public ChatPersonalMessageReceivedEvent(ChatMessageModel model) {
        this.model = model;
    }

    public ChatMessageModel getModel() {
        return model;
    }
}
