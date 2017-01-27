package com.appster.dentamatch.network.response.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 09/01/17.
 */
public class JobTitleList implements Parcelable{


    private int id;
    @SerializedName("jobtitle_name")
    private String jobTitle;
    private boolean isSelected;

    public JobTitleList(){

    }

    protected JobTitleList(Parcel in) {
        id = in.readInt();
        jobTitle = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<JobTitleList> CREATOR = new Creator<JobTitleList>() {
        @Override
        public JobTitleList createFromParcel(Parcel in) {
            return new JobTitleList(in);
        }

        @Override
        public JobTitleList[] newArray(int size) {
            return new JobTitleList[size];
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(jobTitle);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
