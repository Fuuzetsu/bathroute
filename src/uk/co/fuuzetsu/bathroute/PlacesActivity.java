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

public class PlacesActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =
            inflater.inflate(R.layout.places, container, false);


        final ListView lv =
            (ListView) rootView.findViewById(R.id.places_list_view);

        String[] values = new String[] { "3WN", "Library", "3E",
                                         "East Building", "Current location" };

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
