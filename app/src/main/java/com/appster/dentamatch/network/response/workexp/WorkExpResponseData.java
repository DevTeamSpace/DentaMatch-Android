/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.workexp;

import android.os.Parcel;
import android.os.Parcelable;

import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class WorkExpResponseData implements Parcelable {
    public ArrayList<WorkExpRequest> getSaveList() {
        return saveList;
    }

    @SerializedName("list")
    private ArrayList<WorkExpRequest> saveList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.saveList);
    }

    public WorkExpResponseData() {
    }

    protected WorkExpResponseData(Parcel in) {
        this.saveList = in.createTypedArrayList(WorkExpRequest.CREATOR);
    }

    public static final Parcelable.Creator<WorkExpResponseData> CREATOR = new Parcelable.Creator<WorkExpResponseData>() {
        @Override
        public WorkExpResponseData createFromParcel(Parcel source) {
            return new WorkExpResponseData(source);
        }

        @Override
        public WorkExpResponseData[] newArray(int size) {
            return new WorkExpResponseData[size];
        }
    };
}
