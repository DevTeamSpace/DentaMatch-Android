/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.response.PreferredJobLocation;

/**
 * Created by zishan on 22/11/17.
 * To inject activity reference.
 */

public class SelectedPreferredJobLocationData  {
    private int id;
    private String preferredLocationName;
    private int isActive;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreferredLocationName() {
        return preferredLocationName;
    }

    public void setPreferredLocationName(String preferredLocationName) {
        this.preferredLocationName = preferredLocationName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }


}
