package com.appster.dentamatch.network;

/**
 *
 */
public class BaseResponse {

    private boolean success;
    private String message;
    private int statusCode;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
