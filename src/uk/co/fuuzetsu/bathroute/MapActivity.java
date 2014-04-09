package uk.co.fuuzetsu.bathroute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.xmlpull.v1.XmlPullParserException;

import uk.co.fuuzetsu.bathroute.Engine.Node;
import uk.co.fuuzetsu.bathroute.Engine.NodeDeserialiser;
import uk.co.fuuzetsu.bathroute.Engine.NodeManager;
import uk.co.fuuzetsu.bathroute.Engine.Utils;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import fj.data.Option;

public class MapActivity extends Activity implements MapEventsReceiver {

    // mapView object
    private MapView mapView;
    // pre-setting the current Latitude and Longitude at center at the library
    public double centerLat = 51.379932;
    public double centerLong = -2.327943;
    private MyLocationNewOverlay myLocationoverlay;
    private PathOverlay pOverlay;

    // function (current geopoint, map of nodes) returns shortest

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializemap();
        pOverlay = new PathOverlay(Color.BLACK, getApplicationContext());

        NodeDeserialiser nd = new NodeDeserialiser();
        List<Node> nodes = new ArrayList<Node>();
        Resources res = getResources();

        try {
            String nodeText = IOUtils
                    .toString(res.openRawResource(R.raw.nodes));
            nodes = nd.deserialise(nodeText);
            Log.v("Places", "Done deseralising");
        } catch (IOException e) {
            Log.v("Places", "Deserialising failed with IOException");
            Log.v("Places", ExceptionUtils.getStackTrace(e));
        } catch (XmlPullParserException e) {
            Log.v("Places", "Deserialising failed with XmlPullParserException");
            Log.v("Places", ExceptionUtils.getStackTrace(e));
        }

        ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
        Bundle b = getIntent().getExtras();
        String nodeName = b.getString("nodeName");
        centerLat = b.getDouble("nodeLatitude");
        centerLong = b.getDouble("nodeLongitude");
        Integer id = b.getInt("nodeID");

        /*
         * Location we should be getting from the GPS system. Dummy value for
         * now.
         */
        Location start = Utils.makeLocation(-2.323366, 51.378877);

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

        NodeManager nm = new NodeManager(nodes);
        /* Node user chose from the list in PlacesActivity */
        Option<Node> destinationO = nm.getNodeById(id);
        if (destinationO.isNone()) {
            Log.e("MapActivity",
                    String.format("Can't find user-chosen node ID %d", id));
        } else {
            Node destination = destinationO.some();

            Option<List<Node>> p = nm.findPath(start, destination);
            if (p.isSome()) {
                for (Integer i = 0; i < p.some().size(); i++) {
                    // adding a point to pOverlay using location from m
                    pOverlay.addPoint(new GeoPoint(p.some().get(i)
                            .getLocation()));
                }
            } else {
                Log.e("MapActivity", String.format(
                        "Couldn't find a path from %s to node %s",
                        start.toString(), destination.toString()));
            }
        }
        mapView.setLongClickable(true);
        MapEventsOverlay evOverlay = new MapEventsOverlay(this, this);
        mapView.getOverlays().add(evOverlay);

        mapView.getController().setCenter(new GeoPoint(centerLat, centerLong));

        Marker startMarker = new Marker(mapView);

        startMarker.setPosition(new GeoPoint(centerLat, centerLong));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.iconmarka));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        InfoWindow infoWindow = new MyInfoWindow(R.layout.popview, mapView,
                nodeName, "", "");
        startMarker.setInfoWindow(infoWindow);
        mapView.getOverlays().add(myLocationoverlay);
        mapView.getOverlays().add(startMarker);
        // adding overlay
        mapView.getOverlays().add(pOverlay);
        mapView.invalidate();

    }

    public void initializemap() {
        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(18);
        mapView.getController().setZoom(17);
    }

    @Override
    public boolean longPressHelper(IGeoPoint arg0) {
        Log.v("onclick", "onclick");
        Intent i = new Intent(getApplicationContext(),
                EventCreateActivity.class);
        startActivity(i);

        return true;
    }

    @Override
    public boolean singleTapUpHelper(IGeoPoint arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}

class MyMarker extends Marker {

    public MyMarker(MapView mapView) {
        super(mapView);

    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        showInfoWindow();
        super.setOnMarkerClickListener(listener);
    }

}

class MyInfoWindow extends MarkerInfoWindow {
    private final String title;
    private final String description;
    private final String subdescription;

    public MyInfoWindow(int layoutResId, MapView mapView, String title,
            String descrpition, String subdescrition) {
        super(layoutResId, mapView);
        this.description = descrpition;
        this.title = title;
        this.subdescription = subdescrition;
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onOpen(Object arg0) {
        LinearLayout layout = (LinearLayout) mView
                .findViewById(R.id.bubble_layout);

        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView
                .findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView
                .findViewById(R.id.bubble_subdescription);

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtSubdescription.setText(subdescription);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
    }
}
