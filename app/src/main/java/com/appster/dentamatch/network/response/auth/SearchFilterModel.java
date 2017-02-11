package com.appster.dentamatch.network.response.auth;

import java.util.ArrayList;

/**
 * Created by Appster on 07/02/17.
 */

public class SearchFilterModel {
    private int isFulltime;
    private int isParttime;
    private ArrayList<Integer> jobTitle;
    private String lat;
    private String lng;
    private int page;
    private ArrayList<String> parttimeDays;

    public int getIsFulltime() {
        return isFulltime;
    }

    public int getIsParttime() {
        return isParttime;
    }

    public ArrayList<Integer> getJobTitle() {
        return jobTitle;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public int getPage() {
        return page;
    }

    public ArrayList<String> getParttimeDays() {
        return parttimeDays;
    }

    public String getZipCode() {
        return zipCode;
    }

    private String zipCode;
}
