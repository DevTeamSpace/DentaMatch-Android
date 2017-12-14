package com.appster.dentamatch.network.response.PreferredJobLocation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zishan on 22/11/17.
 */

public class PreferredJobLocationData implements Parcelable {
    private int id;
    private String preferredLocationName;
    private int isActive;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreferredLocationName() {
        return preferredLocationName;
    }

    public void setPreferredLocationName(String preferredLocationName) {
        this.preferredLocationName = preferredLocationName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.preferredLocationName);
        dest.writeInt(this.isActive);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public PreferredJobLocationData() {
    }

    protected PreferredJobLocationData(Parcel in) {
        this.id = in.readInt();
        this.preferredLocationName = in.readString();
        this.isActive = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<PreferredJobLocationData> CREATOR = new Creator<PreferredJobLocationData>() {
        @Override
        public PreferredJobLocationData createFromParcel(Parcel source) {
            return new PreferredJobLocationData(source);
        }

        @Override
        public PreferredJobLocationData[] newArray(int size) {
            return new PreferredJobLocationData[size];
        }
    };
}
