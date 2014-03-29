package uk.co.fuuzetsu.bathroute;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;

public class MapActivity extends Activity {



   private MapView mapView;
   public double centerLat = 51.379932;
   public double centerLong = -2.327943;



@Override

 protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        mapView=(MapView) this.findViewById(R.id.mapview);


        mapView.setBuiltInZoomControls(true);

        mapView.setMultiTouchControls(true);

        mapView.setMaxZoomLevel(19);

        mapView.getController().setZoom(17);


        //setting the central coordinates as per attribute value
        mapView.getController().setCenter(new GeoPoint(centerLat,centerLong));

        // show pathOverlay
        PathOverlay pathOverlay = new PathOverlay(Color.RED, this);

        //adding start point ---------------------------------------------- 1

        //start point is Library
        pathOverlay.addPoint(new GeoPoint(centerLat,centerLong));

        // ----------------------------------------------------------------- 2

        //research and development center claverton rooms
        pathOverlay.addPoint(new GeoPoint(51.379509,-2.328469));

        // ----------------------------------------------------------------- 3
        //student center
        pathOverlay.addPoint(new GeoPoint(51.379462,-2.326508));

        //setting the stroke width

        pathOverlay.getPaint().setStrokeWidth(3.0f);

        ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
        OverlayItem olItem = new OverlayItem("Here", "FoundersHall", new GeoPoint(51.379462, -2.326508));
        OverlayItem olItem2 = new OverlayItem("Here", "Library", new GeoPoint(centerLat,centerLong));
        OverlayItem olItem3 = new OverlayItem("Here", "Claverton Rooms", new GeoPoint(51.379509,-2.328469));//marker
        overlayItemArray.add(olItem);
        overlayItemArray.add(olItem2);
        overlayItemArray.add(olItem3);
        MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this, overlayItemArray);
        //adding the overlay thus calling paint

        mapView.getOverlays().add(pathOverlay);
    mapView.getOverlays().add(overlay);
        // refresh map, is this needed?
        mapView.invalidate();



}






}
