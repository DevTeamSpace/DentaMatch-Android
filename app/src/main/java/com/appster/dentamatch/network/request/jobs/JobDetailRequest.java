/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.jobs;

/**
 * Created by Appster on 28/01/17.
 * To inject activity reference.
 */

public class JobDetailRequest {
    private int jobId;
    private double lat, lng;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
