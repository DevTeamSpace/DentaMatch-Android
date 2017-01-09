package com.appster.dentamatch.network;

/**
 * Base class for Api Response.
 */
public class BaseResponse {
    private int status;
    private int statusCode;
    private String message;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {

        return status;
    }
}
