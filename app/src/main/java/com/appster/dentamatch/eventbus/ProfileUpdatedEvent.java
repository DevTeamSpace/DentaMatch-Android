package com.appster.dentamatch.eventbus;

/**
 * Created by Appster on 24/01/17.
 * To stream event related to profile updates.
 */

public class ProfileUpdatedEvent {
    private final boolean mIsProfileUpdated;

    public ProfileUpdatedEvent(boolean isProfileUpdated) {
        mIsProfileUpdated = isProfileUpdated;
    }

    public boolean ismIsProfileUpdated() {
        return mIsProfileUpdated;
    }
}
