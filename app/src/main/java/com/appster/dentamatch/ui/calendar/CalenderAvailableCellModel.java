package com.appster.dentamatch.ui.calendar;

import java.util.Date;


/**
 * Created by virender on 01/02/17.
 */
public class CalenderAvailableCellModel {

    private Date date;
    private boolean isSelected;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
