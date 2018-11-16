/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.util;

import android.text.TextUtils;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;

import java.util.HashMap;

/**
 * Created by virender on 13/01/17.
 * To inject activity reference.
 */
public class WorkExpValidationUtil {
    private static final HashMap<Boolean, String> returnValue = new HashMap<>();

    /**
     * We return a true value in case the validation is not a missing element but rather a missing format
     * like the mobile number format or email validation problem.
     */
    public static HashMap<Boolean, String> checkValidation(int isReference2,
                                                           String selectedJobtitle,
                                                           int expMonth,
                                                           String officeName,
                                                           String officeAddress,
                                                           String officeCity,
                                                           String officeState,
                                                           String reference1Email,
                                                           String reference2Email,
                                                           String reference1Mobile,
                                                           String reference2Mobile,
                                                           String referenceName1,
                                                           String referenceName2) {

        returnValue.clear();

        if (TextUtils.isEmpty(selectedJobtitle)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.blank_job_title_alert));

        } else if (expMonth == 0) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.blank_year_alert));

        } else if (TextUtils.isEmpty(officeName)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.blank_office_name_alert));

        } else if (officeName.length() > Constants.DEFAULT_FIELD_LENGTH) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.office_name_length_alert));

        } else if (TextUtils.isEmpty(officeAddress)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.blank_office_addrerss_alert));

        } else if (officeAddress.length() > Constants.DEFAULT_FIELD_LENGTH) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.office_address_length_alert));

        } else if (TextUtils.isEmpty(officeCity)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.blank_city_alert));

        } else if (TextUtils.isEmpty(officeState)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.empty_state_alert));

        } else if (officeCity.length() > Constants.DEFAULT_FIELD_LENGTH) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.city_length_alert));

        }   else if((!TextUtils.isEmpty(reference1Mobile) || !TextUtils.isEmpty(reference1Email)) && TextUtils.isEmpty(referenceName1)) {
            returnValue.put(false, DentaApp.getInstance().getString(R.string.msg_reference_name_1_blank));

        }else if((!TextUtils.isEmpty(reference2Mobile) || !TextUtils.isEmpty(reference2Email)) && TextUtils.isEmpty(referenceName2)){
            returnValue.put(false, DentaApp.getInstance().getString(R.string.msg_reference_name_2_blank));

        }else if(!TextUtils.isEmpty(reference2Email) && !Utils.isValidEmailAddress(reference2Email)){
            returnValue.put(false, DentaApp.getInstance().getString(R.string.valid_email_alert));

        }else if (!TextUtils.isEmpty(reference1Mobile) && reference1Mobile.length() < 13) {
            returnValue.put(true, DentaApp.getInstance().getString(R.string.valid_mobile_alert));

        }else if(!TextUtils.isEmpty(reference2Mobile) && reference2Mobile.length() < 13){
            returnValue.put(true, DentaApp.getInstance().getString(R.string.valid_mobile_alert));

        }else if (!TextUtils.isEmpty(reference1Email) && !Utils.isValidEmailAddress(reference1Email)) {
            returnValue.put(true, DentaApp.getInstance().getString(R.string.valid_email_alert));

        }else {
            returnValue.put(true, "");
        }


        return returnValue;
    }

    public static WorkExpRequest prepareWorkExpRequest(int isReference2, String action, int jobTitleId, int expMonth, String officeName, String officeAddress, String officeCity,String officeState, String officeReference1Name, String reference1Mobile, String reference1Email, String reference2Email, String reference2Name, String reference2Mobile) {
        WorkExpRequest workExpRequest = new WorkExpRequest();
        workExpRequest.setCity(officeCity);
        workExpRequest.setState(officeState);
        workExpRequest.setOfficeName(officeName);
        workExpRequest.setOfficeAddress(officeAddress);
        workExpRequest.setMonthsOfExpereince(expMonth);
        workExpRequest.setAction(action);
        workExpRequest.setJobTitleId(jobTitleId);
        workExpRequest.setReference1Mobile(reference1Mobile);
        workExpRequest.setReference1Name(officeReference1Name);
        workExpRequest.setReference1Email(reference1Email);
/*
  0 mean view is visible
 */
        if (isReference2 == 0) {
            workExpRequest.setReference2Mobile(reference2Mobile);
            workExpRequest.setReference2Name(reference2Name);
            workExpRequest.setReference2Email(reference2Email);
        }
        return workExpRequest;
    }



}
