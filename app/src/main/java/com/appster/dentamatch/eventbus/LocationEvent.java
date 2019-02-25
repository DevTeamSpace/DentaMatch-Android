/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.eventbus;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ram on 06/01/17.
 * To stream location related data event.
 */

public class LocationEvent {
    private final Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getMessage() {
        return location;
    }

    /**
     * Created by virender on 16/01/17.
     */
    public static class Affiliation implements Parcelable {
        private int affiliationId;
        private String affiliationName;
        private String otherAffiliation;
        private int jobSeekerAffiliationStatus;

        public int getAffiliationId() {
            return affiliationId;
        }

        public int getJobSeekerAffiliationStatus() {
            return jobSeekerAffiliationStatus;
        }

        public void setJobSeekerAffiliationStatus(int jobSeekerAffiliationStatus) {
            this.jobSeekerAffiliationStatus = jobSeekerAffiliationStatus;
        }

        public void setAffiliationId(int affiliationId) {
            this.affiliationId = affiliationId;
        }

        public String getAffiliationName() {
            return affiliationName;
        }

        public void setAffiliationName(String affiliationName) {
            this.affiliationName = affiliationName;
        }

        public String getOtherAffiliation() {
            return otherAffiliation;
        }

        public void setOtherAffiliation(String otherAffiliation) {
            this.otherAffiliation = otherAffiliation;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.affiliationId);
            dest.writeString(this.affiliationName);
            dest.writeString(this.otherAffiliation);
            dest.writeInt(this.jobSeekerAffiliationStatus);
        }

        public Affiliation() {
        }

        protected Affiliation(Parcel in) {
            this.affiliationId = in.readInt();
            this.affiliationName = in.readString();
            this.otherAffiliation = in.readString();
            this.jobSeekerAffiliationStatus = in.readInt();
        }

        public static final Parcelable.Creator<Affiliation> CREATOR = new Parcelable.Creator<Affiliation>() {
            @Override
            public Affiliation createFromParcel(Parcel source) {
                return new Affiliation(source);
            }

            @Override
            public Affiliation[] newArray(int size) {
                return new Affiliation[size];
            }
        };
    }
}