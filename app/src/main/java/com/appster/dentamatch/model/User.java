package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Appster on 23/05/16.
 */
public class User implements Parcelable{
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
    private int id;

    private User(Parcel in) {
        email = in.readString();
        status = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        jobTitle = in.readString();
        jobTitleId = in.readInt();
        profileImage = in.readString();
        profilePic = in.readString();
        userName = in.readString();
        accountID = in.readString();
        accountType = in.readString();
        dentalStateBoard = in.readString();
        licenseNumber = in.readString();
        state = in.readString();
        aboutMe = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        postalCode = in.readString();
        preferredJobLocation = in.readString();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
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
        dest.writeInt(id);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public int getJobTitleID() {
        return jobTitleId;
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
}
