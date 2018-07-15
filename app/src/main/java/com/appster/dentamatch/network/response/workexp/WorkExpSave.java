package com.appster.dentamatch.network.response.workexp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
class WorkExpSave {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("job_title_id")
    private int jobTitleId;
    @SerializedName("months_of_expereince")
    private int monthExp;

    private int id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("reference2_email")
    private String reference2Email;
    @SerializedName("reference2_mobile")
    private String reference2Mobile;
    @SerializedName("reference2_name")
    private String reference2Name;

    @SerializedName("reference1_email")
    private String reference1Email;
    @SerializedName("reference1_mobile")
    private String reference1Mobile;
    @SerializedName("reference1_name")
    private String reference1Name;

    @SerializedName("office_name")
    private String officeName;
    @SerializedName("office_address")
    private String officeAddress;
    @SerializedName("city")
    private String city;


}
