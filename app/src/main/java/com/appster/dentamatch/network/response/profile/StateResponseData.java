/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by atul on 14/11/18.
 * To inject activity reference.
 */
public class StateResponseData implements Parcelable {

    @SerializedName("stateList")
    private ArrayList<StateList> stateList;

    public ArrayList<StateList> getStateList() {
        return stateList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(stateList);
    }

    public static final Parcelable.Creator<StateResponseData> CREATOR = new Parcelable.Creator<StateResponseData>() {
        @Override
        public StateResponseData createFromParcel(Parcel parcel) {
            StateResponseData responseData = new StateResponseData();
            parcel.readTypedList(responseData.stateList, StateList.CREATOR);
            return responseData;
        }

        @Override
        public StateResponseData[] newArray(int i) {
            return new StateResponseData[i];
        }
    };
}
