package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import fj.data.Option;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.fuuzetsu.bathroute.Engine.CommunicationManager;
import uk.co.fuuzetsu.bathroute.Engine.DataStore;
import uk.co.fuuzetsu.bathroute.Engine.Event;
import uk.co.fuuzetsu.bathroute.Engine.Friend;
import uk.co.fuuzetsu.bathroute.Engine.JSONWriter;
import uk.co.fuuzetsu.bathroute.Engine.Utils;

public class EventDetailsActivity extends Activity {

    //store the title
    private TextView title;
    //store event description
    private TextView time;
    private TextView date;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdetails);
        title = (TextView)findViewById(R.id.eventdetails_title);
        title.setText("hello");
        date = (TextView)findViewById(R.id.eventDate);
        time = (TextView)findViewById(R.id.eventTime);
        description = (TextView)findViewById(R.id.eventDescription);
        Button delete = (Button) findViewById(R.id.deleteButton);

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Double lat = b.getDouble("latitude");
                Double lon = b.getDouble("longitude");
                String des = b.getString("description");
                Integer fid = b.getInt("creator");

                Friend u = new Friend(fid);
                Location l = Utils.makeLocation(lon, lat);

                Option<CommunicationManager> cmo =
                    DataStore.getInstance().getCommunicationManager();
                if (cmo.isSome()) {
                    CommunicationManager cm = cmo.some();
                    try {
                        JSONObject o = JSONWriter.deleteEvent(u, l, des);
                        cm.sendMessage(0, o);
                    } catch (JSONException e) {
                        Log.e("EventCreateActivity", "Failed to delete event.");
                    } catch (IOException e) {
                        Log.e("EventCreateActivity",
                              "Failed to send event delete request to the server.");
                    }
                }

                finish();
            }
        });

    }
}
