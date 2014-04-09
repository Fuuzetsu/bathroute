package uk.co.fuuzetsu.bathroute;

import android.util.Log;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import fj.data.Option;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.fuuzetsu.bathroute.Engine.CommunicationManager;
import uk.co.fuuzetsu.bathroute.Engine.DataStore;
import uk.co.fuuzetsu.bathroute.Engine.Friend;
import uk.co.fuuzetsu.bathroute.Engine.JSONWriter;
import uk.co.fuuzetsu.bathroute.Engine.Utils;

public class EventCreateActivity extends Activity {
    private EditText title;
    private EditText date;
    private EditText time;
    private EditText description;
    private Button save;
    private Button cancel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);
        title = (EditText) findViewById(R.id.eventName);
        title.setText("hello");
        date = (EditText) findViewById(R.id.Date);
        time = (EditText) findViewById(R.id.Time);
        description = (EditText) findViewById(R.id.description);
        save = (Button) findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Double lat = b.getDouble("latitude");
                Double lon = b.getDouble("longitude");

                Location l = Utils.makeLocation(lon, lat);
                Friend u = new Friend(0);
                String des = description.getText().toString();
                Option<CommunicationManager> cmo =
                    DataStore.getInstance().getCommunicationManager();
                if (cmo.isSome()) {
                    CommunicationManager cm = cmo.some();
                    try {
                        JSONObject o = JSONWriter.createEvent(u, l, des);
                        cm.sendMessage(0, o);
                    } catch (JSONException e) {
                        Log.e("EventCreateActivity", "Failed to create event.");
                    } catch (IOException e) {
                        Log.e("EventCreateActivity",
                              "Failed to send new event to the server.");
                    }
                }

                finish();
            }
        });
        cancel = (Button) findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
