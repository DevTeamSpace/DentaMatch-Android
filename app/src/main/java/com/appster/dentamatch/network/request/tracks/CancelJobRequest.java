/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.tracks;

/**
 * Created by Appster on 03/02/17.
 * To inject activity reference.
 */

public class CancelJobRequest {
    private int jobId;
    private String cancelReason;

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
