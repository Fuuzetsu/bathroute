package uk.co.fuuzetsu.bathroute;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;

public class CurrentLocation extends Activity {
        private MapView mapView;

        private MyLocationNewOverlay myLocationoverlay;
        private LocationManager myLocationmanger;

        /** Called when the activity is first created. */

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                initializemap();
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

        public void initializemap() {
                mapView = (MapView) this.findViewById(R.id.mapview);
                mapView.setTileSource(TileSourceFactory.MAPNIK);
                mapView.setBuiltInZoomControls(true);
                mapView.setMultiTouchControls(true);
                mapView.setMaxZoomLevel(19);

                mapView.getController().setZoom(17);
        }
}
