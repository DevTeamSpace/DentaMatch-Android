/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.profile;

import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.auth.LicenceVerifiedStatus;

/**
 * Created by virender on 12/01/17.
 * To inject activity reference.
 */
public class LicenseUpdateResponse extends BaseResponse {
    private LicenceVerifiedStatus result;

    public LicenceVerifiedStatus getResult() {
        return result;
    }

    public void setResult(LicenceVerifiedStatus result) {
        this.result = result;
    }

}
