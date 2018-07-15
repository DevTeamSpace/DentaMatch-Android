/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.affiliation;

/**
 * Created by virender on 16/01/17.
 * To inject activity reference.
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
