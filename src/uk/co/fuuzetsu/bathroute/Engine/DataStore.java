package uk.co.fuuzetsu.bathroute.Engine;


import fj.F;
import fj.Unit;
import fj.data.Option;
import java.util.LinkedList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import uk.co.fuuzetsu.bathroute.Engine.Event;
import uk.co.fuuzetsu.bathroute.Engine.Friend;

public class DataStore {

    private static DataStore storeInstance;

    private List<Event> events = new LinkedList<Event>();
    private Option<Friend> ownUser = Option.none();
    private Option<GeoPoint> lastGeoPoint = Option.none();

    private DataStore() { }

    public static DataStore getInstance() {
        if (storeInstance == null) {
            DataStore ds = new DataStore();
            DataStore.storeInstance = ds;
        }
        return DataStore.storeInstance;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public synchronized Unit setEvents(List<Event> es) {
        this.events = es;
        return Unit.unit();
    }

    public Option<Friend> getOwnUser() {
        return this.ownUser;
    }

    public synchronized Unit setOwnUser(final Friend u) {
        this.ownUser = Option.some(u);
        return Unit.unit();
    }

    public synchronized Unit setLastGeoPoint(final GeoPoint l) {
        this.lastGeoPoint = Option.some(l);
        return Unit.unit();
    }

    public synchronized Option<GeoPoint> getLastGeoPoint() {
        return this.lastGeoPoint;
    }


    public Unit modifyUser(F<Option<Friend>, Option<Friend>> f) {
        this.ownUser = f.f(this.ownUser);
        return Unit.unit();
    }

    public Unit modifyEvents(F<List<Event>, List<Event>> f) {
        return setEvents(f.f(this.events));
    }

}
