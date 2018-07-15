/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.request.auth;

/**
 * Created by virender on 11/01/17.
 * To inject activity reference.
 */
class ReferenceRequest {
    private String refrenceName;
    private String phoneNumber;
    private String email;
    public String getReferenceName() {
        return refrenceName;
    }

    public void setRefrenceName(String refrenceName) {
        this.refrenceName = refrenceName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
