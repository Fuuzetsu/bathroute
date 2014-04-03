package uk.co.fuuzetsu.bathroute;
import java.util.ArrayList;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class MapActivity extends Activity {


    //mapView object
    private MapView mapView;
    //pre-setting the current Latitude and Longitude at center at the library
    public double centerLat = 51.379932;
    public double centerLong = -2.327943;
    private MyLocationNewOverlay myLocationoverlay;
    private LocationManager myLocationmanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializemap();
        //list of overlay items
        ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
        Bundle b = getIntent().getExtras();
        String nodeName = b.getString("nodeName");
        centerLat = b.getDouble("nodeLatitude");
        centerLong = b.getDouble("nodeLongitude");

        mapView.getController().setCenter(new GeoPoint(centerLat, centerLong));
        OverlayItem olItem =
            new OverlayItem("No comment", nodeName,
                            new GeoPoint(centerLat,centerLong));
        overlayItemArray.add(olItem);

        MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this, overlayItemArray);
        //to show pin
        mapView.getOverlays().add(overlay);
        // refresh map, is this needed?
        mapView.invalidate();


    }
    public void initializemap()
    {
        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(19);
        mapView.getController().setZoom(17);
    }

}
