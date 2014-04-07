package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import java.util.List;

public class Event {
    private final String description;
    private final List<Friend> attendees;
    private final Friend creator;
    private final Location location;

    public Event(final String description, final Friend creator,
                 final List<Friend> attendees,
                 final Location location) {
        this.description = description;
        this.attendees = attendees;
        this.creator = creator;
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Friend> getAttendees() {
        return this.attendees;
    }

    public Friend getCreator() {
        return this.creator;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return String.format
            ("%d made %s at (%f, %f). %d attendees.",
             this.creator.getKey(),
             this.description,
             this.location.getLongitude(),
             this.location.getLatitude(),
             this.attendees.size());
    }
}
