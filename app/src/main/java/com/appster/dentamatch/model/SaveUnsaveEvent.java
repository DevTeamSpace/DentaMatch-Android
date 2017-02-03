package com.appster.dentamatch.model;

/**
 * Created by Appster on 02/02/17.
 */

public class SaveUnSaveEvent {
    private int JobID;
    private int status;

    public SaveUnSaveEvent(int jobID, int status) {
        this.JobID = jobID;
        this.status = status;

    }

    public int getJobID() {
        return JobID;
    }

    public int getStatus() {
        return status;
    }

}
