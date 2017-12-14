package com.appster.dentamatch.network.request.jobs;

import com.appster.dentamatch.model.SelectedJobTitleModel;
import com.appster.dentamatch.network.response.PreferredJobLocation.SelectedPreferredJobLocationData;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("jobTitles")
    private ArrayList<SelectedJobTitleModel> selectedJobTitles;
    private String address;
    private String state;
    private String city;
    private String country;
    private ArrayList<Integer> preferredJobLocationId;

    public ArrayList<Integer> getPreferredJobLocationId() {
        return preferredJobLocationId;
    }

    public void setPreferredJobLocationId(ArrayList<Integer> preferredJobLocationId) {
        this.preferredJobLocationId = preferredJobLocationId;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<SelectedJobTitleModel> getSelectedJobTitles() {
        return selectedJobTitles;
    }

    public void setSelectedJobTitles(ArrayList<SelectedJobTitleModel> selectedJobTitles) {
        this.selectedJobTitles = selectedJobTitles;
    }

    public void setParttimeDays(ArrayList<String> parttimeDays) {
        this.parttimeDays = parttimeDays;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getPage() {
        return page;
    }

    public ArrayList<Integer> getJobTitle() {
        return jobTitle;
    }

    public int getIsFulltime() {
        return isFulltime;
    }

    public int getIsParttime() {
        return isParttime;
    }

    public ArrayList<String> getParttimeDays() {
        return parttimeDays;
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
