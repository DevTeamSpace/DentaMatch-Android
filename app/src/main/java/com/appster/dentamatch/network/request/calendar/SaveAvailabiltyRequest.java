package com.appster.dentamatch.network.request.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 06/02/17.
 */
public class SaveAvailabiltyRequest {
    private int isFulltime;
    private ArrayList<String> partTimeDays;
    @SerializedName("tempdDates")
    private ArrayList<String> tempDates;

    public int getIsFulltime() {
        return isFulltime;
    }

    public void setIsFulltime(int isFulltime) {
        this.isFulltime = isFulltime;
    }

    public ArrayList<String> getPartTimeDays() {
        return partTimeDays;
    }

    public void setPartTimeDays(ArrayList<String> partTimeDays) {
        this.partTimeDays = partTimeDays;
    }

    public ArrayList<String> getTempDates() {
        return tempDates;
    }

    public void setTempDates(ArrayList<String> tempDates) {
        this.tempDates = tempDates;
    }
}
