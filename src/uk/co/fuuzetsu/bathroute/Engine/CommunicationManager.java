package uk.co.fuuzetsu.bathroute.Engine;

import android.util.Log;
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

public class CommunicationManager {

    private static final String HOST = "10.0.2.2";
    private static final int PORT = 7777;
    private final Socket s;
    private final DataOutputStream sout;
    private final DataInputStream sin;
    private final BufferedReader reader;

    public CommunicationManager(final String host, final int port)
        throws IOException, UnknownHostException {
        this.s = new Socket(host, port);
        this.sout = new DataOutputStream(s.getOutputStream());
        this.sin =  new DataInputStream(s.getInputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.sin));
    }

    public CommunicationManager() throws IOException, UnknownHostException {
        this(CommunicationManager.HOST, CommunicationManager.PORT);
    }

    /* When bool is true, notify online, else offline */
    public JSONObject createOnlineStatusRequest(final Boolean b) {
        JSONObject o = new JSONObject();
        try {
            o.put("tag", "OnlineStatus");
            o.put("contents", b ? "Online" : "Offline");
        } catch (JSONException e) {
            /* This really should never happen. Ever. */
            Log.e("CommunicationManager",
                  "JSONException in createOnlineStatusRequest:\n"
                  + ExceptionUtils.getStackTrace(e));
        }
        return o;
    }

    public void write(byte[] b) throws IOException {
        this.sout.write(b);
        this.sout.flush();
    }

    public void write(String s) throws IOException {
        write(s.getBytes(Charset.forName("UTF-8")));
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
