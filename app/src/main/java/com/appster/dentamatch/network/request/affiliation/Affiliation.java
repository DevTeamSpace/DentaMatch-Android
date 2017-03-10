package com.appster.dentamatch.network.request.affiliation;

/**
 * Created by virender on 16/01/17.
 */
public class Affiliation {
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