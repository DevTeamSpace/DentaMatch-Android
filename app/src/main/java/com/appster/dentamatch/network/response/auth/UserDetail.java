package com.appster.dentamatch.network.response.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ram on 06/01/17.
 */

public class UserDetail implements Parcelable {
    private String id;
    private String firstName;
    private String lastName;
    private String userId;
    private String email;
    private String zipCode;
    private String preferredJobLocation;
    private String licenseNumber;
    private String preferredLocationName;
    private int preferredJobLocationId;
    private String jobtitleName;
    private Integer jobTitileId;
    private int isVerified;


    public int getIsJobSeekerVerified() {
        return isJobSeekerVerified;
    }

    public void setIsJobSeekerVerified(int isJobSeekerVerified) {
        this.isJobSeekerVerified = isJobSeekerVerified;
    }

    private  int isJobSeekerVerified;
    private int profileCompleted;

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }




    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getJobTitileId() {
        return jobTitileId;
    }

    public void setJobTitileId(Integer jobTitileId) {
        this.jobTitileId = jobTitileId;
    }


    public String getPreferredLocationName() {
        return preferredLocationName;
    }

    public void setPreferredLocationName(String preferredLocationName) {
        this.preferredLocationName = preferredLocationName;
    }

    public int getPreferredJobLocationId() {
        return preferredJobLocationId;
    }

    public void setPreferredJobLocationId(int preferredJobLocationId) {
        this.preferredJobLocationId = preferredJobLocationId;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getProfileCompleted() {
        return profileCompleted;
    }

    public String getId() {
        return id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    @SerializedName("accessToken")
    private String userToken;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPreferredJobLocation() {
        return preferredJobLocation;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPreferredJobLocation(String preferredJobLocation) {
        this.preferredJobLocation = preferredJobLocation;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.userId);
        dest.writeString(this.email);
        dest.writeString(this.zipCode);
        dest.writeString(this.preferredJobLocation);
        dest.writeString(this.licenseNumber);
        dest.writeString(this.preferredLocationName);
        dest.writeInt(this.preferredJobLocationId);
        dest.writeString(this.jobtitleName);
        dest.writeValue(this.jobTitileId);
        dest.writeInt(this.isVerified);
        dest.writeInt(this.isJobSeekerVerified);
        dest.writeInt(this.profileCompleted);
        dest.writeString(this.imageUrl);
        dest.writeString(this.userToken);
    }

    protected UserDetail(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userId = in.readString();
        this.email = in.readString();
        this.zipCode = in.readString();
        this.preferredJobLocation = in.readString();
        this.licenseNumber = in.readString();
        this.preferredLocationName = in.readString();
        this.preferredJobLocationId = in.readInt();
        this.jobtitleName = in.readString();
        this.jobTitileId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isVerified = in.readInt();
        this.isJobSeekerVerified = in.readInt();
        this.profileCompleted = in.readInt();
        this.imageUrl = in.readString();
        this.userToken = in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel source) {
            return new UserDetail(source);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };
}
