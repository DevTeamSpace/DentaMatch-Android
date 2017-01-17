package com.appster.dentamatch.network.response.affiliation;

/**
 * Created by virender on 16/01/17.
 */
public class OtherAffiliationRequest {
    private int affiliationId;
    private String otherAffiliation;

    public void setAffiliationId(int affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setOtherAffiliation(String otherAffiliation) {
        this.otherAffiliation = otherAffiliation;
    }
}
