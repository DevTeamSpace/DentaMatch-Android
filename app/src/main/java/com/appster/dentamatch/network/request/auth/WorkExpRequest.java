package com.appster.dentamatch.network.request.auth;

import java.util.ArrayList;

/**
 * Created by virender on 11/01/17.
 */
public class WorkExpRequest {
    private int id;
    private String jobTitle;
    private String exp;


    private int jobTitleId;
    private int monthsOfExpereince;
    private String officeName;
    private String officeAddress;
    private String city;
    private String reference1Name;
    private String reference2Name;
    private String reference1Mobile;
    private String reference2Mobile;
    private String reference1Email;
    private String reference2Email;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getMonthsOfExpereince() {
        return monthsOfExpereince;
    }

    public void setMonthsOfExpereince(int monthsOfExpereince) {
        this.monthsOfExpereince = monthsOfExpereince;
    }

    public String getReference1Name() {
        return reference1Name;
    }

    public void setReference1Name(String reference1Name) {
        this.reference1Name = reference1Name;
    }

    public String getReference2Name() {
        return reference2Name;
    }

    public void setReference2Name(String reference2Name) {
        this.reference2Name = reference2Name;
    }

    public String getReference1Mobile() {
        return reference1Mobile;
    }

    public void setReference1Mobile(String reference1Mobile) {
        this.reference1Mobile = reference1Mobile;
    }

    public String getReference2Mobile() {
        return reference2Mobile;
    }

    public void setReference2Mobile(String reference2Mobile) {
        this.reference2Mobile = reference2Mobile;
    }

    public String getReference1Email() {
        return reference1Email;
    }

    public void setReference1Email(String reference1Email) {
        this.reference1Email = reference1Email;
    }

    public String getReference2Email() {
        return reference2Email;
    }

    public void setReference2Email(String reference2Email) {
        this.reference2Email = reference2Email;
    }

    public int getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    private String action;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }




}
