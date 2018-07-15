/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

/**
 * Created by Appster on 03/02/17.
 * To stream job cancelled events.
 */

public class JobCancelEvent {
    private final int jobID;
    private final String msg;

    public JobCancelEvent(int jobID, String msg) {
        this.jobID = jobID;
        this.msg = msg;
    }

    public int getJobID() {
        return jobID;
    }

    public String getMsg() {
        return msg;
    }

}
