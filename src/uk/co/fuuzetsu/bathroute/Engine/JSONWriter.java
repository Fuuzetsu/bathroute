package uk.co.fuuzetsu.bathroute.Engine;

import org.apache.commons.lang3.exception.ExceptionUtils;
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

    public static JSONObject friend(FriendAction a, Friend u) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("tag", "FriendStatus");
        JSONObject i = new JSONObject();
        switch (a) {
        case ADD: i.put("tag", "Add"); break;
        case REMOVE: i.put("tag", "Remove"); break;
        case BLOCK: i.put("tag", "Block"); break;
        case SHARE: i.put("tag", "Share"); break;
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
