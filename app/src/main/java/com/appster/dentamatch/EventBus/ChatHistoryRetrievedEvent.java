package com.appster.dentamatch.EventBus;

import org.json.JSONArray;

/**
 * Created by Appster on 27/02/17.
 */

public class ChatHistoryRetrievedEvent {
    private JSONArray model;
    public ChatHistoryRetrievedEvent(JSONArray model) {
        this.model = model;
    }

    public JSONArray getModel() {
        return model;
    }
}
