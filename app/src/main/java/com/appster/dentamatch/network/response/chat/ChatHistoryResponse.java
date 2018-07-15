/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.chat;

import com.appster.dentamatch.network.BaseResponse;

/**
 * Created by Appster on 08/02/17.
 * To inject activity reference.
 */

public class ChatHistoryResponse extends BaseResponse {
    private ChatHistoryList result;

    public ChatHistoryList getResult() {
        return result;
    }
}
