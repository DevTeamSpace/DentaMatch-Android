package com.appster.dentamatch.network.request.workexp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by virender on 11/01/17.
 */
public class WorkExpRequest implements Parcelable {
    private String id;
    private String exp;
    private String action;
    private String userId;
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
    private String createdAt;
    private String jobTitleName;

    public WorkExpRequest() { }

    private WorkExpRequest(Parcel in) {
        id = in.readString();
        exp = in.readString();
        action = in.readString();
        userId = in.readString();
        jobTitleId = in.readInt();
        monthsOfExpereince = in.readInt();
        officeName = in.readString();
        officeAddress = in.readString();
        city = in.readString();
        reference1Name = in.readString();
        reference2Name = in.readString();
        reference1Mobile = in.readString();
        reference2Mobile = in.readString();
        reference1Email = in.readString();
        reference2Email = in.readString();
        createdAt = in.readString();
        jobTitleName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(exp);
        parcel.writeString(action);
        parcel.writeString(userId);
        parcel.writeInt(jobTitleId);
        parcel.writeInt(monthsOfExpereince);
        parcel.writeString(officeName);
        parcel.writeString(officeAddress);
        parcel.writeString(city);
        parcel.writeString(reference1Name);
        parcel.writeString(reference2Name);
        parcel.writeString(reference1Mobile);
        parcel.writeString(reference2Mobile);
        parcel.writeString(reference1Email);
        parcel.writeString(reference2Email);
        parcel.writeString(createdAt);
        parcel.writeString(jobTitleName);
    }

    public static final Creator<WorkExpRequest> CREATOR = new Creator<WorkExpRequest>() {
        @Override
        public WorkExpRequest createFromParcel(Parcel in) {
            return new WorkExpRequest(in);
        }

        @Override
        public WorkExpRequest[] newArray(int size) {
            return new WorkExpRequest[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobTitleName() {
        return jobTitleName;
    }

    public void setJobTitleName(String jobTitleName) {
        this.jobTitleName = jobTitleName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
