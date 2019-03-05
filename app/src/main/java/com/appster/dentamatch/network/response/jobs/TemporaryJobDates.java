/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.jobs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by virender on 10/02/17.
 * To inject activity reference.
 */
class TemporaryJobDates implements Parcelable {

    private String jobDate;
    private int recruiterJobId;


    public String getJobDate() {
        return jobDate;
    }

    public int getRecruiterJobId() {
        return recruiterJobId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobDate);
        dest.writeInt(this.recruiterJobId);
    }

    public TemporaryJobDates() {
    }

    protected TemporaryJobDates(Parcel in) {
        this.jobDate = in.readString();
        this.recruiterJobId = in.readInt();
    }

    public static final Parcelable.Creator<TemporaryJobDates> CREATOR = new Parcelable.Creator<TemporaryJobDates>() {
        @Override
        public TemporaryJobDates createFromParcel(Parcel source) {
            return new TemporaryJobDates(source);
        }

        @Override
        public TemporaryJobDates[] newArray(int size) {
            return new TemporaryJobDates[size];
        }
    };
}
