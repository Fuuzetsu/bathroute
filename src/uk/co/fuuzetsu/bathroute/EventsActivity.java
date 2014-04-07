package uk.co.fuuzetsu.bathroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.LinkedList;
import java.util.List;
import uk.co.fuuzetsu.bathroute.Engine.DataStore;
import uk.co.fuuzetsu.bathroute.Engine.Event;

public class EventsActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.events, container, false);

        DataStore ds = DataStore.getInstance();
        List<String> evs = new LinkedList<String>();

        for (Event e : ds.getEvents()) {
            evs.add(e.toString());
        }

        final ListView lv = (ListView) rootView.findViewById(R.id.events_list_view);

        // String[] values = new String[] { "Create New Event", "View Existing Events" };
        String[] values = evs.toArray(new String[0]);

        ArrayAdapter<String> lvadapter
            = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, values);

        lv.setAdapter(lvadapter);

        return rootView;
    }
}
