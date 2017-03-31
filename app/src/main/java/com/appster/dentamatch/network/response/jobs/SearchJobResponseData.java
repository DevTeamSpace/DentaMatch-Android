package com.appster.dentamatch.network.response.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 */

public class SearchJobResponseData implements Parcelable {
    @SerializedName("list")
    private ArrayList<SearchJobModel> jobList;
    private int total;

    protected SearchJobResponseData(Parcel in) {
        jobList = in.createTypedArrayList(SearchJobModel.CREATOR);
        total = in.readInt();
    }

    public static final Creator<SearchJobResponseData> CREATOR = new Creator<SearchJobResponseData>() {
        @Override
        public SearchJobResponseData createFromParcel(Parcel in) {
            return new SearchJobResponseData(in);
        }

        @Override
        public SearchJobResponseData[] newArray(int size) {
            return new SearchJobResponseData[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public ArrayList<SearchJobModel> getList() {
        return jobList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(jobList);
        dest.writeInt(total);
    }
}
