/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.jobs;

/**
 * Created by Appster on 01/02/17.
 * To inject activity reference.
 */

public class SaveUnSaveRequest {
    private int jobId;
    private int status;

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
