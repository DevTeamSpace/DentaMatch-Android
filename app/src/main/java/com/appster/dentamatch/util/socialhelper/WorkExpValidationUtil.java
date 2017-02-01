package com.appster.dentamatch.util.socialhelper;

import android.text.TextUtils;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

/**
 * Created by virender on 13/01/17.
 */
public class WorkExpValidationUtil {


    public static boolean checkValidation(int isReference2, String selectedJobtitle, int expMonth, String officeName, String officeAddress,
                                          String officeCity, String officeReference1Name, String reference1Email, String reference2Email,
                                          String reference2Name, String reference1Mobile, String reference2Mobile) {


        if (TextUtils.isEmpty(selectedJobtitle)) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_job_title_alert));
            return false;
        }
        if (expMonth == 0) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_year_alert));
            return false;
        }
        if (TextUtils.isEmpty(officeName)) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_office_name_alert));
            return false;
        }
        if (officeName.length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.office_name_length_alert));
            return false;
        }

        if (TextUtils.isEmpty(officeAddress)) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_office_addrerss_alert));
            return false;
        }
        if (officeAddress.length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.office_address_length_alert));
            return false;
        }
        if (TextUtils.isEmpty(officeCity)) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_city_alert));
            return false;
        }
        if (officeCity.length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.city_length_alert));
            return false;
        }

//
//        if (TextUtils.isEmpty(officeReference1Name)) {
//            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_refrence_name_alert));
//            return false;
//        }
//        if (!TextUtils.isEmpty(reference1Email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(reference1Email).matches()) {
//            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_email_alert));
//            return false;
//        }
        if (!TextUtils.isEmpty(reference1Email) &&! Utils.isValidEmailAddress(reference1Email)) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_email_alert));
            return false;
        }

        if (!TextUtils.isEmpty(reference1Mobile) && reference1Mobile.length() < 13) {
            Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_mobile_alert));
            return false;
        }
        if (isReference2 == 0) {
//            if (TextUtils.isEmpty(reference2Name)) {
//                Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.blank_refrence_name_alert));
//                return false;
//            }

//            if (!TextUtils.isEmpty(reference2Email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(reference2Email).matches()) {
//                Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_email_alert));
//                return false;
//            }

            if (!TextUtils.isEmpty(reference2Email) && !Utils.isValidEmailAddress(reference2Email)) {
                Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_email_alert));
                return false;
            }
            if (!TextUtils.isEmpty(reference2Mobile) && reference2Mobile.length() < 13) {
                Utils.showToast(DentaApp.getAppContext(), DentaApp.getAppContext().getString(R.string.valid_mobile_alert));
                return false;
            }
        }


        return true;
    }

    public static WorkExpRequest prepareWorkExpRequest(int isReference2, String action, int jobTitleId, int expMonth, String officeName, String officeAddress, String officeCity, String officeReference1Name, String reference1Mobile, String reference1Email, String reference2Email, String reference2Name, String reference2Mobile) {
        WorkExpRequest workExpRequest = new WorkExpRequest();
        workExpRequest.setCity(officeCity);
        workExpRequest.setOfficeName(officeName);
        workExpRequest.setOfficeAddress(officeAddress);
        workExpRequest.setMonthsOfExpereince(expMonth);
        workExpRequest.setAction(action);
        workExpRequest.setJobTitleId(jobTitleId);
        workExpRequest.setReference1Mobile(reference1Mobile);
        workExpRequest.setReference1Name(officeReference1Name);
        workExpRequest.setReference1Email(reference1Email);
/**
 * 0 mean view is visible
 */
        if (isReference2 == 0) {
            workExpRequest.setReference2Mobile(reference2Mobile);
            workExpRequest.setReference2Name(reference2Name);
            workExpRequest.setReference2Email(reference2Email);
        }
        return workExpRequest;
    }

}
