package com.appster.dentamatch.EventBus;

/**
 * Created by Appster on 03/02/17.
 */

public class JobCancelEvent {
    private int jobID;
    private String msg;

    public JobCancelEvent(int jobID, String msg){
        this.jobID = jobID;
        this.msg = msg;
    }

    public int getJobID() {
        return jobID;
    }

    public String getMsg() {
        return msg;
    }
}
