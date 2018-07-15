package com.appster.dentamatch.eventbus;

import org.json.JSONArray;

/**
 * Created by Appster on 27/02/17.
 * To stream chat history retrieval process.
 */

public class ChatHistoryRetrievedEvent {
    private final JSONArray model;

    public ChatHistoryRetrievedEvent(JSONArray model) {
        this.model = model;
    }

    public JSONArray getModel() {
        return model;
    }
}
