package uk.co.fuuzetsu.bathroute.engine;

import android.location.Location;

public class Utils {

    private Utils() {}

    public static Location makeLocation(final Double longitude,
                                        final Double latitude) {
        Location l = new Location("hardcoded");
        l.setLongitude(longitude);
        l.setLatitude(latitude);
        return l;
    }
}
