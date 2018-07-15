/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class ProfileSchoolModel {
    private int id;
    private int childId;
    private String schoolName;
    private String schoolChildName;
    private String schoolTitle;
    private String otherSchooling;
    private int yearOfGraduation;

    public int getId() {
        return id;
    }

    public int getChildId() {
        return childId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolChildName() {
        return schoolChildName;
    }

    public String getSchoolTitle() {
        return schoolTitle;
    }

    public String getOtherSchooling() {
        return otherSchooling;
    }

    public int getYearOfGraduation() {
        return yearOfGraduation;
    }
}
