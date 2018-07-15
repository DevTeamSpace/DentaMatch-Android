package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.model.SelectedJobTitleModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Appster on 07/02/17.
 * To inject activity reference.
 */

public class SearchFilterModel {
    private String isFulltime;
    private String isParttime;
    private ArrayList<Integer> jobTitle;
    private String lat;
    private String lng;
    private String city;
    private String country;
    private String state;
    private int page;
    private String address;
    private ArrayList<String> parttimeDays;
    private String zipCode;


    @SerializedName("jobTitles")
    private ArrayList<SelectedJobTitleModel> selectedJobTitles;

    public ArrayList<SelectedJobTitleModel> getSelectedJobTitles() {
        return selectedJobTitles;
    }

    public String getAddress() {
        return address;
    }

    public String getIsFulltime() {
        return isFulltime;
    }

    public String getIsParttime() {
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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }
}
