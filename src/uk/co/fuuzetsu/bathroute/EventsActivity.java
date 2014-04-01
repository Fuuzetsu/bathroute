package uk.co.fuuzetsu.bathroute;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventsActivity extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.events, container, false);

        final ListView lv = (ListView) rootView.findViewById(R.id.events_list_view);
        //Grab friend list and place here
        String[] values = new String[] { "Create New Event", "View Existing Events" };

        ArrayAdapter<String> lvadapter
            = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, values);

        lv.setAdapter(lvadapter);

        return rootView;
    }
}
