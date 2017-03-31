package com.appster.dentamatch.network.request.tracks;

/**
 * Created by Appster on 02/02/17.
 */

public class TrackJobsRequest {
    private double lat;
    private double lng;
    private int page;
    private double type;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setType(double type) {
        this.type = type;
    }
}
