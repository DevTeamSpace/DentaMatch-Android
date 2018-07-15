/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

import com.appster.dentamatch.ui.messages.ChatMessageModel;

/**
 * Created by Appster on 13/02/17.
 * To stream global messages.
 */

class GlobalMessageReceivedEvent {
    private final ChatMessageModel model;

    public GlobalMessageReceivedEvent(ChatMessageModel model) {
        this.model = model;
    }

    public ChatMessageModel getModel() {
        return model;
    }
}
