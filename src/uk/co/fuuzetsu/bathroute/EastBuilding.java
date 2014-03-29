package uk.co.fuuzetsu.bathroute;



import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;

public class EastBuilding extends Activity {


//51.37876/-2.32309
   private MapView mapView;
   public double centerLat = 51.378766;
   public double centerLong = -2.323099;



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


        //setting the stroke width

        pathOverlay.getPaint().setStrokeWidth(3.0f);

        ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
        OverlayItem olItem = new OverlayItem("Mon-Sat 8AM - 12AM", "East Building", new GeoPoint(centerLat,centerLong));

        overlayItemArray.add(olItem);

        MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this, overlayItemArray);
        //adding the overlay thus calling paint

        mapView.getOverlays().add(pathOverlay);
    mapView.getOverlays().add(overlay);
        // refresh map, is this needed?
        mapView.invalidate();



}






}
