package com.appster.dentamatch.interfaces;

/**
 * Created by virender on 15/01/17.
 * To inject activity reference.
 */
public interface JobTitleSelectionListener {
    void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired);

}
