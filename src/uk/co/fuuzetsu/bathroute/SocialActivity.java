package uk.co.fuuzetsu.bathroute;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

import android.support.v4.app.Fragment;

public class SocialActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =
            inflater.inflate(R.layout.social, container, false);


        final ListView lv =
            (ListView) rootView.findViewById(R.id.social_list_view);

        String[] values = new String[] { "Friends", "Events" };

        ArrayAdapter<String> lvadapter
            = new ArrayAdapter<String>(rootView.getContext(),
                                       android.R.layout.simple_list_item_1,
                                       values);

        lv.setAdapter(lvadapter);


        return rootView;
    }
}
