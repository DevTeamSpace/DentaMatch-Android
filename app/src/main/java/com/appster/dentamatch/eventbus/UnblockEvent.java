/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

/**
 * Created by bawenderyandra on 10/03/17.
 * To inject activity reference.
 */

public class UnblockEvent {
    private final boolean status;

    public UnblockEvent(boolean status){
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
