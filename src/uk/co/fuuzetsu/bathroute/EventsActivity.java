package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.fuuzetsu.bathroute.Engine.DataStore;
import uk.co.fuuzetsu.bathroute.Engine.Event;

public class EventsActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.events, container,
                                               false);

        final ListView lv = (ListView) rootView.findViewById(R.id.events_list_view);

        DataStore ds = DataStore.getInstance();
        List<Event> evs = ds.getEvents();
        final Map<Integer, Event> evm = new HashMap<Integer, Event>();
        String[] values = new String[evs.size()];

        for (Integer i = 0; i < evs.size(); i++) {
            evm.put(i, evs.get(i));
            values[i] = evs.get(i).getDescription();
        }

        ArrayAdapter<String> lvadapter =
            new ArrayAdapter<String>(rootView.getContext(),
                                     android.R.layout.simple_list_item_1, values);

        lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                    Intent i = new Intent(rootView.getContext(), EventDetailsActivity.class);
                    Event ev = evm.get(position);
                    i.putExtra("latitude", ev.getLocation().getLatitude());
                    i.putExtra("longitude", ev.getLocation().getLongitude());
                    i.putExtra("description", ev.getDescription());
                    i.putExtra("creator", ev.getCreator().getKey());
                    startActivity(i);
                }
            });

        lv.setAdapter(lvadapter);

        return rootView;
    }
}
