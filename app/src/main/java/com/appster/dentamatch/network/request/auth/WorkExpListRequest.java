package com.appster.dentamatch.network.request.auth;

/**
 * Created by virender on 13/01/17.
 */
public class WorkExpListRequest {
    private int limit;
    private int start;


    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
