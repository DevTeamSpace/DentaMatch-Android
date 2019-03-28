/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Appster on 01/02/17.
 * To inject activity reference.
 */

@Parcelize
data class JobDetailModel(
    var id: Int = 0,
    var jobType: Int = 0,
    var noOfJobs: Int = 0,
    var isMonday: Int = 0,
    var isTuesday: Int = 0,
    var isWednesday: Int = 0,
    var isThursday: Int = 0,
    var isFriday: Int = 0,
    var isSaturday: Int = 0,
    var isSunday: Int = 0,
    var templateName: String? = null,
    var templateDesc: String? = null,
    var workEverydayStart: String? = null,
    var workEverydayEnd: String? = null,
    var mondayStart: String? = null,
    var mondayEnd: String? = null,
    var tuesdayStart: String? = null,
    var tuesdayEnd: String? = null,
    var wednesdayStart: String? = null,
    var wednesdayEnd: String? = null,
    var thursdayStart: String? = null,
    var thursdayEnd: String? = null,
    var fridayStart: String? = null,
    var fridayEnd: String? = null,
    var saturdayStart: String? = null,
    var saturdayEnd: String? = null,
    var sundayStart: String? = null,
    var sundayEnd: String? = null,
    @SerializedName("jobtitleName")
    var jobTitleName: String? = null,
    var officeName: String? = null,
    var officeDesc: String? = null,
    var address: String? = null,
    @SerializedName("zipcode")
    var zipCode: Int = 0,
    var latitude: Double = 0.toDouble(),
    var longitude: Double = 0.toDouble(),
    var createdAt: String? = null,
    var jobPostedTimeGap: Int = 0,
    var officeTypeName: String? = null,
    var distance: Double = 0.toDouble(),
    var jobTypeString: String? = null,
    var isApplied: Int = 0,
    var isSaved: Int = 0,
    var partTimeDays: String? = null,
    var jobTypeDates: ArrayList<String>? = null,
    var percentaSkillsMatch: String? = null,
    var matchedSkills: String? = null,
    var templateSkillsCount: String? = null,
    var payRate: Int = 0
) : Parcelable
