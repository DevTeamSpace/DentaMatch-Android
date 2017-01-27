package com.appster.dentamatch.network.response.jobs;

import com.appster.dentamatch.network.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Appster on 27/01/17.
 */

public class SearchJobResponse extends BaseResponse {
    private ArrayList<SearchJobModel> list;

    public ArrayList<SearchJobModel> getList() {
        return list;
    }
}
