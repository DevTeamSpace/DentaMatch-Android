package com.appster.dentamatch.network.request.profile;

/**
 * Created by virender on 21/01/17.
 */
public class ProfileUpdateRequest {
    private int zipcode;
    private int jobTitileId;
    private String firstName;
    private String lastName;
    private String latitude;
    private String longitude;
    private String preferredJobLocation;
    private String aboutMe;

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setJobTitileId(int jobTitileId) {
        this.jobTitileId = jobTitileId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
