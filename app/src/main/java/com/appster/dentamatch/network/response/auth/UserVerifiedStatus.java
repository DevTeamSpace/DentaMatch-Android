package com.appster.dentamatch.network.response.auth;

import com.appster.dentamatch.network.BaseResponse;

/**
 * Created by abhaykant on 11/12/17.
 */

public class UserVerifiedStatus extends BaseResponse{

    public IsVerifiedStatus getResult() {
        return result;
    }

    public void setResult(IsVerifiedStatus result) {
        this.result = result;
    }

    private IsVerifiedStatus result;
}
