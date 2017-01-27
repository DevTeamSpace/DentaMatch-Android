package com.appster.dentamatch.network.request.jobs;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 */

public class SearchJobRequest {
    private String lat;
    private String lng;
    private String zipCode;
    private int page;
    private ArrayList<Integer> jobTitle;
    private int isFulltime;
    private int isParttime;
    private ArrayList<String> parttimeDays;

    public void setParttimeDays(ArrayList<String> parttimeDays) {
        this.parttimeDays = parttimeDays;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setJobTitle(ArrayList<Integer> jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setIsFulltime(int isFulltime) {
        this.isFulltime = isFulltime;
    }

    public void setIsParttime(int isParttime) {
        this.isParttime = isParttime;
    }


}
