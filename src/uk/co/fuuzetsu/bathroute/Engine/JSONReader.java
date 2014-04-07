package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import android.util.Log;
import fj.F;
import fj.Unit;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.fuuzetsu.bathroute.Engine.Utils;

public class JSONReader {

    private JSONReader() { }

    /* Takes ‘callbacks’ for each possible FriendAction we could see. */
    public static Unit onFriendAction(final String message,
                                      final F<Integer, Unit> onAdd,
                                      final F<Integer, Unit> onRemove,
                                      final F<Integer, Unit> onBlock,
                                      final F<Location, Unit> onShare,
                                      final F<List<Event>, Unit> onEvents) {
        try {
            JSONObject oo = new JSONObject(message);
            String topTag = oo.getString("tag");
            if ("EventList".equals(topTag)) {
                JSONArray ar = oo.getJSONArray("contents");
                List<Event> evs = new ArrayList<Event>(ar.length());

                for (Integer i = 0; i < ar.length(); i++) {
                    JSONObject ev = ar.getJSONObject(i);
                    String des = ev.getString("_evDescription");
                    Friend creator = new Friend(ev.getInt("_evCreator"));
                    JSONArray ari = ev.getJSONArray("_evLocation");
                    Location l = Utils.makeLocation(ari.getDouble(1), ari.getDouble(0));
                    evs.add(new Event(des, creator, new ArrayList<Friend>(), l));
                }

                return onEvents.f(evs);

            } else if ("FriendStatus".equals(topTag)) {
                JSONObject o = oo.getJSONObject("contents");
                String tag = o.getString("tag");
                if ("Add".equals(tag)) {
                    Integer uid = o.getInt("contents");
                    return onAdd.f(uid);
                } else if ("Remove".equals(tag)) {
                    Integer uid = o.getInt("contents");
                    return onRemove.f(uid);
                } else if ("Block".equals(tag)) {
                    Integer uid = o.getInt("contents");
                    return onBlock.f(uid);
                } else if ("Share".equals(tag)) {
                    JSONArray ar = o.getJSONArray("contents");
                    Double latitude = ar.getDouble(0);
                    Double longitude = ar.getDouble(1);
                    return onShare.f(Utils.makeLocation(longitude, latitude));
                }
            }


            Log.v("JSONReader", "No handler for " + message);

            return Unit.unit();

        } catch (JSONException e) {
            Log.e("JSONReader", "Broke on" + message);
            Log.e("JSONReader", ExceptionUtils.getStackTrace(e));
            return Unit.unit();
        }
    }
}
