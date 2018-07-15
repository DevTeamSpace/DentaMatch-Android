/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.calendar;

import java.util.Date;


/**
 * Created by virender on 01/02/17.
 * To inject activity reference.
 */
class CalenderAvailableCellModel {

    private Date mDate;
    private boolean mIsSelected;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}
