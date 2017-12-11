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

    private  int isJobSeekerVerified;
    private  int profileCompleted;

    public int getIsJobSeekerVerified() {
        return isJobSeekerVerified;
    }

    public void setIsJobSeekerVerified(int isJobSeekerVerified) {
        this.isJobSeekerVerified = isJobSeekerVerified;
    }

    public int getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(int profileCompleted) {
        this.profileCompleted = profileCompleted;
    }


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
        dest.writeTypedList(this.jobList);
        dest.writeInt(this.total);
        dest.writeInt(this.isJobSeekerVerified);
        dest.writeInt(this.profileCompleted);
    }

    public SearchJobResponseData() {
    }

    protected SearchJobResponseData(Parcel in) {
        this.jobList = in.createTypedArrayList(SearchJobModel.CREATOR);
        this.total = in.readInt();
        this.isJobSeekerVerified = in.readInt();
        this.profileCompleted = in.readInt();
    }

    public static final Creator<SearchJobResponseData> CREATOR = new Creator<SearchJobResponseData>() {
        @Override
        public SearchJobResponseData createFromParcel(Parcel source) {
            return new SearchJobResponseData(source);
        }

        @Override
        public SearchJobResponseData[] newArray(int size) {
            return new SearchJobResponseData[size];
        }
    };
}
