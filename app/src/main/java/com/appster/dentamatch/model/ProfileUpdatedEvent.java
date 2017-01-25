package com.appster.dentamatch.model;

/**
 * Created by Appster on 24/01/17.
 */

public class ProfileUpdatedEvent {
    private boolean mIsProfileUpdated;

    public ProfileUpdatedEvent(boolean isProfileUpdated){
        mIsProfileUpdated = isProfileUpdated;
    }

    public boolean ismIsProfileUpdated() {
        return mIsProfileUpdated;
    }
}
