package com.appster.dentamatch.ui.calendar;

import java.util.Date;

public class EventObjects {
    private int id;
    private String message;
    private Date date;
    private int dayOfMonth;
    public EventObjects(String message, Date date, int dayOfMonth) {
        this.message = message;
        this.date = date;
        this.dayOfMonth = dayOfMonth;
    }
    public EventObjects(int id, String message, Date date) {
        this.date = date;
        this.message = message;
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public int getDayOfMonth() {
        return dayOfMonth;
    }
//    public int setDayOfMonth(int dayOfMonth) {
//        return this.dayOfMonth=dayOfMonth;
//    }
    public String getMessage() {
        return message;
    }
    public Date getDate() {
        return date;
    }
}