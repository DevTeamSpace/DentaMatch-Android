/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

/**
 * Created by Appster on 08/02/17.
 * To inject activity reference.
 */

public class SocketConnectionEvent {
    private final boolean connectionStatus;

    public SocketConnectionEvent(boolean status){
        this.connectionStatus = status;
    }

    public boolean getStatus() {
        return connectionStatus;
    }
}
