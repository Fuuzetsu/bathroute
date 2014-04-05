package uk.co.fuuzetsu.bathroute;

import java.util.ArrayList;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Marker.OnMarkerClickListener;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class MapActivity extends Activity {

	// mapView object
	private MapView mapView;
	// pre-setting the current Latitude and Longitude at center at the library
	public double centerLat = 51.379932;
	public double centerLong = -2.327943;
	private MyLocationNewOverlay myLocationoverlay;
	private LocationManager myLocationmanger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializemap();
		// list of overlay items
		ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
		Bundle b = getIntent().getExtras();
		String nodeName = b.getString("nodeName");
		centerLat = b.getDouble("nodeLatitude");
		centerLong = b.getDouble("nodeLongitude");
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

		// mapView.getController().setCenter(new GeoPoint(centerLat,
		// centerLong));
		OverlayItem olItem = new OverlayItem("No comment", nodeName,
				new GeoPoint(centerLat, centerLong));
		overlayItemArray.add(olItem);

		MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this,
				overlayItemArray);
		// to show pin
		mapView.getOverlays().add(overlay);
		mapView.getOverlays().add(myLocationoverlay);
		mapView.invalidate();

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
	private String title;
	private String description;
	private String subdescription;

	public MyInfoWindow(int layoutResId, MapView mapView, String title,
			String descrpition, String subdescrition) {
		super(layoutResId, mapView);
		this.description = descrpition;
		this.title = title;
		this.subdescription = subdescrition;
	}

	public void onClose() {
	}

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