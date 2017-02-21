package com.appster.dentamatch.network.response.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Model class to hold Login response data.
 */
public class LoginResponseData {

   @SerializedName("userDetails")
    private UserDetail userDetail;
    private SearchFilterModel searchFilters;

    public SearchFilterModel getSearchFilters() {
        return searchFilters;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }
}
