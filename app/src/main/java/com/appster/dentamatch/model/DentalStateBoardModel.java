/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by virender on 19/01/17.
 * To inject activity reference.
 */
public class DentalStateBoardModel implements Parcelable {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public DentalStateBoardModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
    }

    protected DentalStateBoardModel(Parcel in) {
        this.imageUrl = in.readString();
    }

    public static final Creator<DentalStateBoardModel> CREATOR = new Creator<DentalStateBoardModel>() {
        @Override
        public DentalStateBoardModel createFromParcel(Parcel source) {
            return new DentalStateBoardModel(source);
        }

        @Override
        public DentalStateBoardModel[] newArray(int size) {
            return new DentalStateBoardModel[size];
        }
    };
}
