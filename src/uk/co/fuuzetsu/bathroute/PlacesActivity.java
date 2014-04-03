package uk.co.fuuzetsu.bathroute;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =
            inflater.inflate(R.layout.places, container, false);


        final ListView lv =
            (ListView) rootView.findViewById(R.id.places_list_view);

        Log.v("Main", "deserialising");
        NodeDeserialiser nd = new NodeDeserialiser();
        List<Node> nodes = new ArrayList<Node>();
        Resources res = getResources();

        try {
            String nodeText = IOUtils.toString(res.openRawResource(R.raw.nodes));
            nodes = nd.deserialise(nodeText);
            Log.v("Places", "Done deseralising");
            Log.v("Places", nodes.toString());
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

        ArrayAdapter<String> lvadapter
            = new ArrayAdapter<String>(rootView.getContext(),
                                       android.R.layout.simple_list_item_1,
                                       values);

        AdapterView.OnItemClickListener ls =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
                    if (pos == 4) {



                        Intent i = new Intent(rootView.getContext(),
                                              MapActivity.class);
                        i.putExtra("curr", true);
                        startActivity(i);
                    } else if(pos == 3)
                        {

                            Intent i = new Intent(rootView.getContext(),
                                                  MapActivity.class);

                            i.putExtra("eb", true);


                            startActivity(i);
                        }
                    else if(pos == 2)
                        {

                            Intent i = new Intent(rootView.getContext(),
                                                  MapActivity.class);
                            i.putExtra("3e", true);
                            startActivity(i);
                        }
                    else if(pos == 1)
                        {

                            Intent i = new Intent(rootView.getContext(),
                                                  MapActivity.class);

                            i.putExtra("lib", true);
                            startActivity(i);
                        }
                    else if(pos == 0)
                        {

                            Intent i = new Intent(rootView.getContext(),
                                                  MapActivity.class);

                            i.putExtra("3wn", true);
                            startActivity(i);
                        }
                    else {
                        Log.v("PlacesActivity",
                              String.format("Clicked on pos %d", pos));
                    }
                }
            };

        lv.setOnItemClickListener(ls);
        lv.setAdapter(lvadapter);


        return rootView;
    }
}
