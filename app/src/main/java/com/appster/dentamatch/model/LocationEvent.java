package com.appster.dentamatch.model;

import android.location.Location;

/**
 * Created by ram on 06/01/17.
 */

public class LocationEvent {
    private final Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getMessage() {
        return location;
    }
}
