package uk.co.fuuzetsu.bathroute;

import android.R.integer;
import android.R.string;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.xmlpull.v1.XmlPullParserException;
import uk.co.fuuzetsu.bathroute.Engine.Node;
import uk.co.fuuzetsu.bathroute.Engine.NodeDeserialiser;
import uk.co.fuuzetsu.bathroute.Engine.NodeManager;

public class PlacesActivity extends Fragment {

    private AutoCompleteTextView searchField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.places, container,
                false);

        final ListView lv = (ListView) rootView
                .findViewById(R.id.places_list_view);

        Log.v("Main", "deserialising");
        NodeDeserialiser nd = new NodeDeserialiser();
        List<Node> nodes = new ArrayList<Node>();
        Resources res = getResources();

        try {
            String nodeText = IOUtils
                    .toString(res.openRawResource(R.raw.nodes));
            nodes = nd.deserialise(nodeText);
            Log.v("Places", "Done deseralising");
        } catch (IOException e) {
            Log.v("Places", "Deserialising failed with IOException");
            Log.v("Places", ExceptionUtils.getStackTrace(e));
        } catch (XmlPullParserException e) {
            Log.v("Places", "Deserialising failed with XmlPullParserException");
            Log.v("Places", ExceptionUtils.getStackTrace(e));
        }

        NodeManager nm = new NodeManager(nodes);
        final Map<Integer, Node> m = nm.toSortedMap();

        String[] values = new String[m.size()];

        for (Integer i = 0; i < m.size(); i++) {
            /* toSortedMap assures we have some name. */
            values[i] = m.get(i).getName().some();
        }

      ArrayAdapter<String> lvadapter = new ArrayAdapter<String>(
                rootView.getContext(), android.R.layout.simple_list_item_1,
                values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( rootView.getContext(),
                android.R.layout.simple_dropdown_item_1line, values);
        searchField = (AutoCompleteTextView) rootView.findViewById(R.id.search);
        searchField.setAdapter(adapter);
        // user will see locations after one character
        searchField.setThreshold(1);

        // when user clicks enter after inserting word
        searchField.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String str = searchField.getText() + "";
                    // finding id by name of string
                    int id = findIdByName(m, str);

                    if (id != -1) {

                        Intent i = new Intent(rootView.getContext(),
                                MapActivity.class);
                        i.putExtra("nodeName", m.get(id).getName().some());
                        i.putExtra("nodeLatitude", m.get(id).getLocation()
                                .getLatitude());
                        i.putExtra("nodeLongitude", m.get(id).getLocation()
                                .getLongitude());
                        startActivity(i);
                    } else {
                        searchField.setText("");
                    }

                    return true;

                }
                return false;
            }
        });

        AdapterView.OnItemClickListener ls = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                    long id) {

                if (pos < m.size() && pos >= 0) {
                    Intent i = new Intent(rootView.getContext(),
                            MapActivity.class);

                    i.putExtra("nodeName", m.get(pos).getName().some());
                    i.putExtra("nodeLatitude", m.get(pos).getLocation()
                            .getLatitude());
                    i.putExtra("nodeLongitude", m.get(pos).getLocation()
                            .getLongitude());
                    startActivity(i);

                } else {
                    Log.v("PlacesActivity", String.format(
                            "Clicked on unavailable pos: %d", pos));
                }
            }
        };

        lv.setOnItemClickListener(ls);
        lv.setAdapter(lvadapter);

        return rootView;
    }

    // finds node ID by node name used when user enters name
    public int findIdByName(Map<Integer, Node> m, String name) {

        for (int i = 0; i < m.size(); i++) {
            if (m.get(i).getName().some().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
