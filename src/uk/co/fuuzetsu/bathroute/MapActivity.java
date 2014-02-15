package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;

import java.lang.Math;

import uk.co.fuuzetsu.bathroute.engine.Map;
import uk.co.fuuzetsu.bathroute.engine.Utils;

public class MapActivity extends Activity {

    private Map m;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* We set the co-ordinates coresponding to the bottom-left corner
           and top-right corner of our image. We can work out the rest that
           way.
        */
        final Location bl = Utils.makeLocation(-2.33556, 51.37515);
        final Location tr = Utils.makeLocation(-2.31754, 51.38239);

        setContentView(R.layout.map);
        ImageView iv = (ImageView) findViewById(R.id.map_view);

        if (Utils.containsLocation(Utils.makeLocation(1d, 1d), bl, tr)) {
            this.m = new Map(getResources().getDrawable(R.drawable.map), bl, tr);
        } else {
            this.m = new Map(getResources().getDrawable(R.drawable.no_map), bl, tr);
        }

        iv.setImageDrawable(this.m.getDrawableMap());
        Log.v("MapActivity", "Started MapActivity!");
    }
}
