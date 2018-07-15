/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 23/05/16.
 * To inject activity reference.
 */
public class UserModel implements Parcelable{
    private  String email;
    private String status;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String jobTitle;



    @SerializedName("jobTitileId")
    private int jobTitleId;
    private String profileImage;
    private String profilePic;
    private String userName;
    private String accountID;
    private String accountType;
    private String dentalStateBoard;
    private String licenseNumber;
    private String state;
    private String aboutMe;
    private String latitude;
    private String longitude;
    @SerializedName("zipcode")
    private String postalCode;
    private String preferredJobLocation;
    private String preferredCity;
    private String preferredState;
    private String preferredCountry;
    private String preferredJobStateCity;
    private int id;
    private String jobtitleName;
    private  int isJobSeekerVerified;
    private int preferredJobLocationId;
    private String preferredLocationName;
    private int profileCompleted;
    private int isVerified;
    private int isCompleted;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }


    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }



    public int getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(int profileCompleted) {
        this.profileCompleted = profileCompleted;
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


    public int getIsJobSeekerVerified() {
        return isJobSeekerVerified;
    }

    public void setIsJobSeekerVerified(int isJobSeekerVerified) {
        this.isJobSeekerVerified = isJobSeekerVerified;
    }


    /*  @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(status);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(jobTitle);
        dest.writeInt(jobTitleId);
        dest.writeString(profileImage);
        dest.writeString(profilePic);
        dest.writeString(userName);
        dest.writeString(accountID);
        dest.writeString(accountType);
        dest.writeString(dentalStateBoard);
        dest.writeString(licenseNumber);
        dest.writeString(state);
        dest.writeString(aboutMe);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(postalCode);
        dest.writeString(preferredJobLocation);
        dest.writeString(preferredJobStateCity);
        dest.writeInt(id);
    }*/


    public int getJobTitleId() {
        return jobTitleId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDentalStateBoard() {
        return dentalStateBoard;
    }

    public void setDentalStateBoard(String dentalStateBoard) {
        this.dentalStateBoard = dentalStateBoard;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }


    public String getPreferredJobLocation() {
        return preferredJobLocation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPreferredJobLocation(String preferredJobLocation) {
        this.preferredJobLocation = preferredJobLocation;
    }

    public String getPreferredCity() {
        return preferredCity;
    }

    public void setPreferredCity(String preferredCity) {
        this.preferredCity = preferredCity;
    }

    public String getPreferredState() {
        return preferredState;
    }

    public void setPreferredState(String preferredState) {
        this.preferredState = preferredState;
    }

    public String getPreferredCountry() {
        return preferredCountry;
    }

    public void setPreferredCountry(String preferredCountry) {
        this.preferredCountry = preferredCountry;
    }



    public String getPreferredJobStateCity() {
        return preferredJobStateCity;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public UserModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.status);
        dest.writeValue(this.userId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.jobTitle);
        dest.writeInt(this.jobTitleId);
        dest.writeString(this.profileImage);
        dest.writeString(this.profilePic);
        dest.writeString(this.userName);
        dest.writeString(this.accountID);
        dest.writeString(this.accountType);
        dest.writeString(this.dentalStateBoard);
        dest.writeString(this.licenseNumber);
        dest.writeString(this.state);
        dest.writeString(this.aboutMe);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.postalCode);
        dest.writeString(this.preferredJobLocation);
        dest.writeString(this.preferredCity);
        dest.writeString(this.preferredState);
        dest.writeString(this.preferredCountry);
        dest.writeString(this.preferredJobStateCity);
        dest.writeInt(this.id);
        dest.writeString(this.jobtitleName);
        dest.writeInt(this.isJobSeekerVerified);
        dest.writeInt(this.preferredJobLocationId);
        dest.writeString(this.preferredLocationName);
        dest.writeInt(this.profileCompleted);
        dest.writeInt(this.isVerified);
        dest.writeInt(this.isCompleted);
        dest.writeString(this.accessToken);
    }

    protected UserModel(Parcel in) {
        this.email = in.readString();
        this.status = in.readString();
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.jobTitle = in.readString();
        this.jobTitleId = in.readInt();
        this.profileImage = in.readString();
        this.profilePic = in.readString();
        this.userName = in.readString();
        this.accountID = in.readString();
        this.accountType = in.readString();
        this.dentalStateBoard = in.readString();
        this.licenseNumber = in.readString();
        this.state = in.readString();
        this.aboutMe = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.postalCode = in.readString();
        this.preferredJobLocation = in.readString();
        this.preferredCity = in.readString();
        this.preferredState = in.readString();
        this.preferredCountry = in.readString();
        this.preferredJobStateCity = in.readString();
        this.id = in.readInt();
        this.jobtitleName = in.readString();
        this.isJobSeekerVerified = in.readInt();
        this.preferredJobLocationId = in.readInt();
        this.preferredLocationName = in.readString();
        this.profileCompleted = in.readInt();
        this.isVerified = in.readInt();
        this.isCompleted = in.readInt();
        this.accessToken = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
