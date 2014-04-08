package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.Fragment;

public class EventsActivity extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.events, container,
                false);

        final ListView lv = (ListView) rootView
                .findViewById(R.id.events_list_view);
        
        String[] values = new String[] { "Example Event"};
        
        ArrayAdapter<String> lvadapter = new ArrayAdapter<String>(
                rootView.getContext(), android.R.layout.simple_list_item_1,
                values);
        
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            	Intent i = new Intent(rootView.getContext(), EventDetailsActivity.class);
            	startActivity(i);
            }
        });

        lv.setAdapter(lvadapter);
        
		return rootView;
	}
}

        
