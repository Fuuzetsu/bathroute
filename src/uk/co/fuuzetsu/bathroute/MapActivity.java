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
        
        //User clicks 3WN
        if(this.getIntent().getExtras().getBoolean("3wn"))
        {
           //1. set the latitudes to 3WN
        	centerLat = 51.380533;
   		    centerLong = -2.329622;
   		    mapView.getController().setCenter(new GeoPoint(centerLat,centerLong));
   		    OverlayItem olItem = new OverlayItem("Placement Lectures", "3WN", new GeoPoint(centerLat,centerLong));
   		    overlayItemArray.add(olItem);
        }
        //LIB
        else if(this.getIntent().getExtras().getBoolean("lib"))
        {
        	  centerLat = 51.379932;
              centerLong = -2.327943;
              mapView.getController().setCenter(new GeoPoint(centerLat,centerLong));
     		  OverlayItem olItem = new OverlayItem("Open 24 hours", "LIB", new GeoPoint(centerLat,centerLong));
     		  overlayItemArray.add(olItem);
        }
        //3E
        else if(this.getIntent().getExtras().getBoolean("3e"))
        {
        	centerLat = 51.380148;
            centerLong = -2.327317;
            mapView.getController().setCenter(new GeoPoint(centerLat,centerLong));
   		    OverlayItem olItem = new OverlayItem("Lecture Halls", "3e", new GeoPoint(centerLat,centerLong));
   		    overlayItemArray.add(olItem);
        }
        //EB
        else if(this.getIntent().getExtras().getBoolean("eb"))
        {
        	centerLat = 51.378766;
        	centerLong = -2.323099;
        	mapView.getController().setCenter(new GeoPoint(centerLat,centerLong));
   		    OverlayItem olItem = new OverlayItem("Department of Computer Science", "eb", new GeoPoint(centerLat,centerLong));
   		    overlayItemArray.add(olItem);
        }
        //CURRENT.LOC - this has to change
        else if(this.getIntent().getExtras().getBoolean("curr"))
        {
        	  myLocationoverlay = new MyLocationNewOverlay(this, mapView);

              myLocationoverlay.enableFollowLocation();
              myLocationoverlay.enableMyLocation();

              myLocationoverlay.setDrawAccuracyEnabled(false);
              myLocationoverlay.runOnFirstFix(new Runnable() {
                      @Override
                      public void run() {
                              mapView.getController().animateTo(
                                              myLocationoverlay.getMyLocation());
                      }
              });
              mapView.getOverlays().add(myLocationoverlay);
            
        }
        
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
