package com.appster.dentamatch.eventbus;

/**
 * Created by Appster on 02/02/17.
 * To store save un-save event.
 */

public class SaveUnSaveEvent {
    private final int JobID;
    private final int status;

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
