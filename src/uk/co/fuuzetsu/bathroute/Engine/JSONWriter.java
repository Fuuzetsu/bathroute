package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.fuuzetsu.bathroute.Engine.Friend;

public class JSONWriter {

    public static enum FriendAction { ADD, REMOVE, BLOCK, SHARE }
    public static enum OnlineRequest { ONLINE, OFFLINE }

    private JSONWriter() { }

    public static JSONObject online(OnlineRequest online) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("tag", "OnlineStatus");
        o.put("contents",
              JSONWriter.OnlineRequest.ONLINE == online ? "Online" : "Offline");
        return o;
    }

    /* ID announcement is just OnlineStatus ServerMessage with
     * recipient being set to desiredID */
    public static JSONObject announceId(Integer i) throws JSONException {
        JSONObject o = new JSONObject();
        JSONObject in = online(JSONWriter.OnlineRequest.ONLINE);
        o.put("request", in);
        o.put("recipient", i);
        return o;
    }

    private static JSONObject eventWriter(final String tag, final Friend u,
                                         final Location l, final String des)
        throws JSONException {
        JSONObject inn = new JSONObject();
        Double lat = l.getLatitude();
        Double lon = l.getLongitude();

        inn.put("_evLocation", lat);
        inn.accumulate("_evLocation", lon);
        inn.accumulate("_evCreator", u.getKey());
        inn.accumulate("_evDescription", des);

        JSONObject in = new JSONObject();
        in.put("tag", tag);
        in.put("contents", inn);

        JSONObject i = new JSONObject();
        i.put("tag", "EventStatus");
        i.put("contents", in);

        JSONObject o = new JSONObject();
        o.put("request", i);
        o.put("recipient", 0);
        return o;
    }

    /* Here the ‘u’ passed in is the client itself which tells the server where to
       send back the list of events. */
    public static JSONObject requestEvents(final Friend u) throws JSONException {
        JSONObject in = new JSONObject();
        in.put("tag", "EventGet");
        in.put("contents", new JSONArray());

        JSONObject i = new JSONObject();
        i.put("tag", "EventStatus");
        i.put("contents", in);

        JSONObject o = new JSONObject();
        o.put("request", i);
        o.put("recipient", u.getKey());
        return o;
    }

    public static JSONObject createEvent(final Friend u, final Location l,
                                         final String des) throws JSONException {
        return eventWriter("EventAdd", u, l, des);
    }

    public static JSONObject deleteEvent(final Friend u, final Location l,
                                         final String des) throws JSONException {
        return eventWriter("EventDelete", u, l, des);
    }

    public static JSONObject location(final Friend u, final Location l) throws JSONException {
        JSONObject in = new JSONObject();
        in.put("tag", "Share");
        Double lat = l.getLatitude();
        Double lon = l.getLongitude();
        in.put("contents", lat);
        in.accumulate("contents", lon);


        JSONObject i = new JSONObject();
        i.put("tag", "FriendStatus");
        i.put("contents", in);

        JSONObject o = new JSONObject();
        o.put("request", i);
        o.put("recipient", u.getKey());
        return o;
    }

    public static JSONObject friend(FriendAction a, Friend u) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("tag", "FriendStatus");
        JSONObject i = new JSONObject();
        switch (a) {
        case ADD: i.put("tag", "Add"); break;
        case REMOVE: i.put("tag", "Remove"); break;
        case BLOCK: i.put("tag", "Block"); break;
        case SHARE:
            i.put("tag", "Share"); break;
        }

        i.put("contents", u.getKey());
        o.put("contents", i);
        return o;
    }

    public static JSONObject alias(String s) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("tag", "AliasStatus");
        o.put("contents", s);
        return o;
    }
}
