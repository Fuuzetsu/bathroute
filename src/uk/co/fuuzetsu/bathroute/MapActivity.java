package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Display;
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
        final ImageView iv = (ImageView) findViewById(R.id.map_view);
        final Display d = getWindowManager().getDefaultDisplay();

        final Resources r = getResources();

        this.m = new Map(r, bl, tr, d, R.drawable.map, R.drawable.no_map);

        /* Some dummy locations */
        final Location stv = Utils.makeLocation(-2.32446, 51.37756);
        final Location lib = Utils.makeLocation(-2.32798, 51.37989);

        iv.setImageBitmap(this.m.getBitmap(stv));
        Log.v("MapActivity", "Started MapActivity!");
    }
}
