package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import android.util.Log;
import fj.F;
import fj.Unit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.fuuzetsu.bathroute.Engine.JSONReader;

public class CommunicationManager {

    private static final String HOST = "10.0.2.2";
    private static final int PORT = 7777;
    private final Socket s;
    private final DataOutputStream sout;
    private final BufferedReader reader;

    public CommunicationManager(final String host, final int port)
        throws IOException, UnknownHostException {
        this.s = new Socket(host, port);
        this.sout = new DataOutputStream(s.getOutputStream());
        this.reader = new BufferedReader
            (new InputStreamReader(s.getInputStream()));
    }

    public CommunicationManager() throws IOException, UnknownHostException {
        this(CommunicationManager.HOST, CommunicationManager.PORT);
    }

    public Thread listener() {
        return new Thread() {
            @Override
            public void run() {
                reader();
            }
        };
    }

    public void reader() {
        try {
            Log.v("CommunicationManager",
                  "Started reader()");

            /* Callbacks */
            F<Integer, Unit> n = new F<Integer, Unit>() {
                @Override
                public Unit f(Integer i) {
                    Log.v("CommunicationManager",
                          "Received something from user " + i.toString());
                    return Unit.unit();
                }
            };

            F<Location, Unit> shareC = new F<Location, Unit>() {
                @Override
                public Unit f(Location l) {
                    Log.v("CommunicationManager",
                          "Received a location: " + l.toString());
                    return Unit.unit();
                }
            };

            String socketInput;
            while ((socketInput = reader.readLine()) != null && socketInput != null) {
                Log.v("CommunicationManager",
                      "Received a message: " + socketInput);
                JSONReader.onFriendAction(socketInput, n, n, n, shareC);
            }

            Log.v("CommunicationManager",
                  "After reader() loop!");

        } catch (IOException  e) {
            Log.e("CommunicationManager",
                  "IOException in reader():\n"
                  + ExceptionUtils.getStackTrace(e));
        }
    }

    public void write(String s) throws IOException {
        Log.v("CommunicationManager", "Sending message: " + s);
        this.sout.write(s.getBytes(Charset.forName("UTF-8")));
        this.sout.flush();
    }

    public void write(JSONObject js) throws IOException {
        write(js.toString());
    }

    public JSONObject read() throws JSONException, IOException {
        return new JSONObject(reader.readLine());
    }

    public void sendMessage(Integer rec, JSONObject o) throws IOException {
        try {
            /* Create the JSON structure */
            o.put("recipient", rec);
            write(o);

        }  catch (JSONException e) {
            Log.e("CommunicationManager",
                  "JSONException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }
    }
}
