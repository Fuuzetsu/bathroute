package uk.co.fuuzetsu.bathroute.Engine;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.content.res.Resources;
import android.location.Location;
import android.view.Display;

import uk.co.fuuzetsu.bathroute.Engine.Utils;

public class Map {

    private Resources res;
    private Bitmap bmap;
    private Location bl, tr;
    private Display disp;
    private int oobImg;

    public Map(Resources res, final Location bl, final Location tr,
               final Display disp, final int imageId, final int oobImg) {
        this.res = res;
        this.bl = bl;
        this.tr = tr;
        this.disp = disp;

        Point ds = new Point();
        disp.getSize(ds);

        this.bmap = Utils.decodeSampledBitmapFromResource(this.res, imageId,
                                                          ds.x, ds.y);
        this.oobImg = oobImg;
    }

    public Bitmap getBitmap() {
        return this.bmap;
    }

    public Bitmap getBitmap(final Location l) {
        Point ds = new Point();
        disp.getSize(ds);

        if (this.containsLocation(l)) {
            Point p = this.locToMapPoint(l);
            Integer cropX = p.x - ds.x/2;
            Integer cropY = p.y - ds.y/2;

            int h = bmap.getHeight();
            int w = bmap.getWidth();

            cropX = cropX < 0 ? 0 : cropX;
            cropY = cropY < 0 ? 0 : cropY;

            return Bitmap.createBitmap(this.bmap, cropX, cropY, ds.x, ds.y);

        } else {
            return Utils.decodeSampledBitmapFromResource(this.res,
                                                         oobImg, ds.x, ds.y);
        }
    }

    public Point locToMapPoint(final Location l) {
        final Integer h = this.bmap.getHeight();
        final Integer w = this.bmap.getWidth();

        /* The size of our map in longitude and latitude interval */
        final Location tl = Utils.makeLocation(this.bl.getLongitude(), this.tr.getLatitude());

        final Integer newH = (int)
            (h.doubleValue() - h.doubleValue() * ((l.getLatitude() - this.bl.getLatitude())
                                            / (this.tr.getLatitude() - this.bl.getLatitude())));

        final Integer newW = (int)
            (w.doubleValue() * ((l.getLongitude() - this.bl.getLongitude())
                             / (this.tr.getLongitude() - this.bl.getLongitude())));

        return new Point(newW, newH);

    }

    public Boolean containsLocation(final Location l) {
        return Utils.containsLocation(l, this.bl, this.tr);
    }
}
