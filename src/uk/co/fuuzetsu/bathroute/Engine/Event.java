package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import java.util.List;

public class Event {
    private final String description;
    private final List<Friend> attendees;
    private final Friend creator;

    public Event(final String description, final Friend creator,
                 final List<Friend> attendees) {
        this.description = description;
        this.attendees = attendees;
        this.creator = creator;
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

}
