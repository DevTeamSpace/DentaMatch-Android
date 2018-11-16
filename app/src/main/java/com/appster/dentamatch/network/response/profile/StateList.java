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

/**
 * Created by atul on 14/11/18.
 * To inject activity reference.
 */
public class StateList implements Parcelable {

    private String stateName;
    private boolean isSelected;

    public String getStateName() {
        return stateName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeString(stateName);
    }

    public static final Parcelable.Creator<StateList> CREATOR = new Parcelable.Creator<StateList>() {
        @Override
        public StateList createFromParcel(Parcel parcel) {
            StateList stateList = new StateList();
            stateList.isSelected = parcel.readByte() != 0;
            stateList.stateName = parcel.readString();
            return stateList;
        }

        @Override
        public StateList[] newArray(int i) {
            return new StateList[i];
        }
    };
}
