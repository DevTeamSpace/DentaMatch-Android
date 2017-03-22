package com.appster.dentamatch.eventbus;

import android.location.Location;

/**
 * Created by ram on 06/01/17.
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
    public static class Affiliation {
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


    }
}
