package uk.co.fuuzetsu.bathroute.engine;

import android.graphics.drawable.Drawable;
import android.location.Location;

import uk.co.fuuzetsu.bathroute.engine.Utils;

public class Map {

    private Drawable drawable;
    private Location bl, tr;

    public Map(Drawable mapDrawable, final Location bl, final Location tr) {
        this.drawable = mapDrawable;
        this.bl = bl;
        this.tr = tr;
    }

    public Drawable getDrawableMap() {
        return this.drawable;
    }

    public Drawable getDrawableMap(final Location l) {
        return this.drawable;
    }

    public Boolean containsLocation(final Location l) {
        return Utils.containsLocation(l, this.bl, this.tr);
    }
}
