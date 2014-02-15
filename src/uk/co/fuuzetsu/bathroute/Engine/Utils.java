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

    public static Boolean containsLocation(final Location l, final Location bl,
                                           final Location tr) {
        Double lg = l.getLongitude();
        Double lt = l.getLatitude();
        return lg <= bl.getLongitude() && lg >= tr.getLongitude()
            && lt >= bl.getLatitude() && lt <= bl.getLatitude();
    }
}
