package com.appster.dentamatch.network.response.jobs;

import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.network.BaseResponse;

/**
 * Created by Appster on 01/02/17.
 * To inject activity reference.
 */

public class JobDetailResponse extends BaseResponse {
    private JobDetailModel result;

    public JobDetailModel getResult() {
        return result;
    }

}
