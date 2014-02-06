package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;



public class MapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Log.v("MapActivity", "Started MapActivity!");
    }
}
