package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import java.util.List;

public class Event {
    private final String description;
    private final List<Friend> attendees;

    public Event(final String description, final List<Friend> attendees) {
        this.description = description;
        this.attendees = attendees;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Friend> getAttendees() {
        return this.attendees;
    }

}
