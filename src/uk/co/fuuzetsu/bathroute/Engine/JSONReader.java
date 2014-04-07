package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import android.util.Log;
import fj.F;
import fj.Unit;
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
                                      final F<Location, Unit> onShare) {
        try {
            JSONObject o = new JSONObject(message).getJSONObject("contents");
            String tag = o.getString("tag");
            if ("Add".equals(tag)) {
                Integer uid = o.getInt("contents");
                return onAdd.f(uid);
            }
            if ("Remove".equals(tag)) {
                Integer uid = o.getInt("contents");
                return onRemove.f(uid);
            }
            if ("Block".equals(tag)) {
                Integer uid = o.getInt("contents");
                return onBlock.f(uid);
            } else if ("Share".equals(tag)) {
                JSONArray ar = o.getJSONArray("contents");
                Double latitude = ar.getDouble(0);
                Double longitude = ar.getDouble(1);
                return onShare.f(Utils.makeLocation(longitude, latitude));
            }

            return Unit.unit();

        } catch (JSONException e) {
            Log.e("JSONReader", "Broke on" + message);
            Log.e("JSONReader", ExceptionUtils.getStackTrace(e));
            return Unit.unit();
        }
    }
}
