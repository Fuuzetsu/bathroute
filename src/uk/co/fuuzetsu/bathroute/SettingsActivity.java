package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.view.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import android.view.ViewGroup;

public class SettingsActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.settings, container, false);

        return rootView;
    }

    // @Override
    // public View onCreate(Bundle savedInstanceState) {
    //     super.onCreate(savedInstanceState);
    //     setContentView(R.layout.settings);

    //     final ListView lv = (ListView) findViewById(R.id.settings_list_view);

    //     String[] values = new String[] { "Image", "Favourites", "Position broadcasting",
    //                                      "Map", "Storage", "About" };

    //     ArrayAdapter<String> lvadapter
    //         = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

    //     lv.setAdapter(lvadapter);
    // }
}
