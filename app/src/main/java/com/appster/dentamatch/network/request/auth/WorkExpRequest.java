package com.appster.dentamatch.network.request.auth;

import java.util.ArrayList;

/**
 * Created by virender on 11/01/17.
 */
public class WorkExpRequest {
    private int id;
    private String jobTitle;
    private String exp;
    private ArrayList<ReferenceRequest> referenceAerialist;


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

    public ArrayList<ReferenceRequest> getReferenceAerialist() {
        return referenceAerialist;
    }

    public void setReferenceAerialist(ArrayList<ReferenceRequest> referenceAerialist) {
        this.referenceAerialist = referenceAerialist;
    }


}
