package com.appster.dentamatch.network.request.certificates;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 16/01/17.
 */
public class UpdateCertificates {

    private int id;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
