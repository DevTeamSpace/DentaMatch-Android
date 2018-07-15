/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.workexp;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class WorkExpListRequest {
    private int limit;
    private int start;

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
