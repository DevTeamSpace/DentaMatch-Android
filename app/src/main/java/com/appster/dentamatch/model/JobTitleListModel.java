package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleListModel implements Parcelable{


    private int id;
    @SerializedName("jobtitleName")
    private String jobTitle;
    private boolean isSelected;

    public JobTitleListModel(){

    }

    protected JobTitleListModel(Parcel in) {
        id = in.readInt();
        jobTitle = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<JobTitleListModel> CREATOR = new Creator<JobTitleListModel>() {
        @Override
        public JobTitleListModel createFromParcel(Parcel in) {
            return new JobTitleListModel(in);
        }

        @Override
        public JobTitleListModel[] newArray(int size) {
            return new JobTitleListModel[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(jobTitle);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}