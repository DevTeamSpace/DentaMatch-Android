package com.appster.dentamatch.model;

/**
 * Created by ram on 29/09/16.
 */
public enum JobStatus {
    PENDING("1"),
    UNDERPROCESS("2"),
    COMPLETED("3"),
    CANCELLED_BY_SEEKER("4"),
    CANCELLED_BY_PROVIDER("5");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
